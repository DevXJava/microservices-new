package com.programmingtechie.inventoryservice.service;
import com.programmingtechie.inventory.exceptions.InventoryException;
import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public Inventory saveInventory(Inventory inventory){
        if (inventory.getSkuCode() == null || inventory.getSkuCode().isBlank()) {
            throw new InventoryException("SKU Code cannot be empty");
        }
        if (inventory.getQuantity() == null || inventory.getQuantity() < 0) {
            throw new InventoryException("Quantity cannot be negative");
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode){
        log.info("Wait Started");
        //Thread.sleep(10000);
        log.info("Wait Ended");
        return  inventoryRepository.findBySkuCodeIn(skuCode).stream()
               .map(inventory ->
                   InventoryResponse.builder().skuCode(inventory.getSkuCode())
                           .isInStock(inventory.getQuantity()>0)
                           .build()).toList();
    }

    public Inventory updateInventory(Inventory inventoryNew, Long id){
        Inventory inventoryAvail = inventoryRepository.findById(id).orElseThrow(()->new InventoryException("Inventory not available with id: "+id));
        inventoryAvail.setSkuCode(inventoryNew.getSkuCode());
        inventoryAvail.setQuantity(inventoryNew.getQuantity());
        return inventoryRepository.save(inventoryAvail);

    }

    public void deleteInventoryById(Long id){
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()->new InventoryException("Inventory not available with id : "+id));
        inventoryRepository.delete(inventory);
    }
}
