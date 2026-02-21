package com.programmingtechie.order.exceptions;

public class OrderException extends RuntimeException {
    private String message;

    public OrderException(String message){
        this.message = message;
    }
}
