package com.programmingtechie.productservice.controller;

import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
private final ProductService productService;
    @PostMapping("/saveproduct")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
     productService.createProduct(productRequest);
     log.info("product saved !!!");
    }

    @GetMapping("/getallproducts")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        log.info("Got product details !");
        return productService.getAllProducts();
    }

    @GetMapping("/getproductbyid/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id){
       Product product = productService.getProductById(id);
       return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PutMapping("/updateproduct/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable String id) {
        return ResponseEntity.ok(productService.updateProduct(product, id));
    }

    @DeleteMapping("/deleteproductbyid/{id}")
    public ResponseEntity<?> productDeleteById(@PathVariable String id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("product deleted successfully with id : "+id);
    }





}
