package com.programmingtechie.orderservice.controller;
import com.programmingtechie.order.exceptions.OrderException;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name ="inventory",fallbackMethod="fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        log.info("data is {} ",orderRequest.getOrderLineItemsDtoList());
       return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops! something went wrong, place order after some time! ");

    }

    @GetMapping("/allorders")
    public ResponseEntity<?> getAllOrders(){
        List<Order> list = orderService.getAllOrders();
        if(!list.isEmpty()){
            return ResponseEntity.ok(list);

        }
        throw new OrderException("no orders !!!");
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable long id ){
      Order order = orderService.getOrderById(id);
      if(order!=null){
          return new ResponseEntity<>(order,HttpStatus.OK);
      }
      throw new OrderException("order data not found !!!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable long id){
        orderService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order, @PathVariable long id) {

        Order updatedOrder = orderService.update(order, id);
        return ResponseEntity.ok(updatedOrder);
    }



}
