package com.prosoft.service;

import com.prosoft.dto.PersonDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PersonConsumerService {

    @KafkaListener(topics = "person-topic", groupId = "json-consumer-group")
    public void consumePerson(PersonDto person) {
        System.out.println("=== JSON CONSUMER === Received person: " + person);
    }
}