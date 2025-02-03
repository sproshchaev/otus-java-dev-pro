package com.prosoft;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PersonReceiver {
    @RabbitListener(queues = "personQueue")
    public void receivePerson(@Payload Person person) {
        System.out.println("Received: " + person);
    }
}