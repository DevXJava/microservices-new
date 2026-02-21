package com.programmingtechie.global.exception;

import com.programmingtechie.inventory.exceptions.InventoryException;
import com.programmingtechie.notification.exceptions.NotificationException;
import com.programmingtechie.order.exceptions.OrderException;
import com.programmingtechie.product.exceptions.ProductException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(RuntimeException ex, HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(),req.getRequestURI(),
                LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(RuntimeException ex,HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),req.getRequestURI(),
                LocalDateTime.now(Clock.systemDefaultZone()));
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ErrorResponse> handleNotificationException(RuntimeException ex,HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),req.getRequestURI(),
                LocalDateTime.now(Clock.systemDefaultZone()));
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InventoryException.class)
    public ResponseEntity<?> handleInventoryException(RuntimeException ex,HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),req.getRequestURI(),
                LocalDateTime.now(Clock.systemDefaultZone()));
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    //Catch All Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex,HttpServletRequest req){
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(),req.getRequestURI(),
                LocalDateTime.now(Clock.systemDefaultZone()));
        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    //We can handle it in single method

//    @ExceptionHandler({ProductException.class, OrderException.class, InventoryException.class, NotificationException.class})
//    public ResponseEntity<ErrorResponse> handleCustomExceptions(RuntimeException ex, HttpServletRequest request) {
//        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI(),
//                LocalDateTime.now(Clock.systemDefaultZone())
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }



}
