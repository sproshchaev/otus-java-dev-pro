package com.prosoft;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class PersonSender {
    private final RabbitTemplate rabbitTemplate;

    public PersonSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendPerson(Person person) {
        rabbitTemplate.convertAndSend(person);
    }
}

