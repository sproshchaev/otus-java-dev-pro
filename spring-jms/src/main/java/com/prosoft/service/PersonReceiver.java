package com.prosoft.service;

import com.prosoft.domain.Person;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Аннотация @RabbitListener используется для упрощения работы с RabbitMQ в Spring. Вместо того чтобы вручную настраивать
 * MessageListenerContainer и слушатели, с помощью @RabbitListener вы можете аннотировать методы, которые будут автоматически вызываться при получении сообщений из очереди
 */
@Service
public class PersonReceiver {
    @RabbitListener(queues = "personQueue")
    public void receivePerson(@Payload Person person) {
        System.out.println("Received: " + person);
    }
}