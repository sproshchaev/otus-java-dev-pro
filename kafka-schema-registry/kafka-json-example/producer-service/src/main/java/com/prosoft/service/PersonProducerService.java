package com.prosoft.service;

import com.prosoft.dto.PersonDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PersonProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PersonProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPerson(String topic, PersonDto person) {
        kafkaTemplate.send(topic, person);
    }
}