package com.programming.techie.notification.exceptions;

public class NotificationException extends RuntimeException{

    private String message;
    public NotificationException(String message){
        this.message=message;
    }
}
