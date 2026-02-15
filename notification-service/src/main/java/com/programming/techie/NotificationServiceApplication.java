package com.programming.techie;

import com.programming.techie.common.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;


@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        System.out.println("Hello NotificationServiceApplication !!! ................ {} ");
         //send out an email notification
        log.info("### RECEIVED EVENT ### {}", orderPlacedEvent);
        log.info("Received Notification for Order - {} ",orderPlacedEvent.getOrderNumber());

    }
}
