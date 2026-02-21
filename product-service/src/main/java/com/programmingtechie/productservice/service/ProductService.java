package com.programmingtechie.productservice.service;

import com.programmingtechie.product.exceptions.ProductException;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("product {} is saved  ",product.getId());
    }

    public List<ProductResponse> getAllProducts() {
       List<Product>  products = productRepository.findAll();
      return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public Product getProductById(String id){
       return productRepository.findById(id).orElseThrow(()-> new ProductException("product is not avail with id : "+id));
    }

    public Product updateProduct(Product newProduct,String id){
        Product productAvail = productRepository.findById(id).orElseThrow(()->new ProductException("product is not available with id : "+id));
        productAvail.setName(newProduct.getName());
        productAvail.setDescription(newProduct.getDescription());
        productAvail.setPrice(newProduct.getPrice());
        return productRepository.save(productAvail);
    }

    public void deleteProductById(String id){
        Product product = productRepository.findById(id).orElseThrow(()->new ProductException("product is not available with id : "+id));
        productRepository.delete(product);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
