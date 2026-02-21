package com.programmingtechie.product.exceptions;

public class ProductException extends RuntimeException{
    private String message;
    public ProductException(String message){
        this.message=message;
    }
}
