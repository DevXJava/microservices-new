package com.programming.techie.inventory.exceptions;

public class InventoryException extends RuntimeException{
    private String message;
    public InventoryException(String message){
        this.message=message;

    }
}
