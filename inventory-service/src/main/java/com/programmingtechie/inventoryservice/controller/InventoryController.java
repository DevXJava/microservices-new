package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> saveInventory(@RequestBody Inventory inventory) {
        Inventory saved = inventoryService.saveInventory(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInInStock(@RequestParam List<String> skuCode){
       log.info("Got all inventory !");
       return inventoryService.isInStock(skuCode);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@RequestBody Inventory inventory,@PathVariable Long id){
     return ResponseEntity.ok(inventoryService.updateInventory(inventory,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventoryById(@PathVariable Long id){
        inventoryService.deleteInventoryById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
