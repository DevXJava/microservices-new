package com.programmingtechie.orderservice.service;

import brave.Span;
import brave.Tracer;
import com.programming.techie.common.event.OrderPlacedEvent;
import com.programming.techie.order.exceptions.OrderException;
import com.programmingtechie.orderservice.dto.InventoryResponse;
import com.programmingtechie.orderservice.dto.OrderDto;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest){
       Order order = new Order();
       order.setOrderNumber(UUID.randomUUID().toString());

       List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDtoList()
               .stream().map(this :: mapToDto)
               .toList();
       order.setOrderLineItemsList(orderLineItems);

      List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

      Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
      try(Tracer.SpanInScope spanInScope = tracer.withSpanInScope(inventoryServiceLookup.start())){
          //call the inventory service, and place order if a product is in stock
          InventoryResponse[] inventoryResponses= webClientBuilder.build().get()
                  .uri("http://inventory-service:8082/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                  .retrieve()
                  .bodyToMono(InventoryResponse[].class)
                  .block();  //block() method synchronous method

          boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
          log.info("All products data is : {}  ",allProductsInStock);
          if(allProductsInStock) {
              orderRepository.save(order);
              log.info("### SENDING EVENT ### {}", order.getOrderNumber());
              kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()))
                      .whenComplete((result, ex) -> {
                          if (ex == null) {
                              log.info("✅ Sent to Kafka offset={}",
                                      result.getRecordMetadata().offset());
                          } else {
                              log.error("❌ Kafka send failed", ex);
                          }
                      });
              log.info("message is sent by topic : {} ",order.getOrderNumber());
              return "Order placed successfully !";
          }else{
              throw new IllegalArgumentException("product is not in stock please tyr again later !");
          }
        }finally {
         inventoryServiceLookup.finish();
      }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }


    public List<Order> getAllOrders(){
         return orderRepository.findAll();

    }

    public Order getOrderById(long id){
        return orderRepository.findById(id).orElseThrow(()-> new OrderException("order not found with id : "+id));

    }

    public void deleteById(long id){
       Order order = orderRepository.findById(id).orElseThrow(()->new OrderException("Order not found with : "+id));
       orderRepository.delete(order);
    }


    public Order update(Order orderNew,long id){

        Order orderAvailable = orderRepository.findById(id).orElseThrow(()->new OrderException("Order not found with id: " + id));

        orderAvailable.setOrderNumber(orderNew.getOrderNumber());

        //clear old items
        orderAvailable.getOrderLineItemsList().clear();
        // Add new items
        orderAvailable.getOrderLineItemsList()
                .addAll(orderNew.getOrderLineItemsList());

        return orderRepository.save(orderAvailable);

    }



}
