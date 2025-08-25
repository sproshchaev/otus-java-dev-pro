package com.prosoft.controller;

import com.prosoft.dto.PersonDto;
import com.prosoft.service.PersonProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonProducerService personProducerService;

    public PersonController(PersonProducerService personProducerService) {
        this.personProducerService = personProducerService;
    }

    @PostMapping
    public String sendPerson(@RequestBody PersonDto person) {
        personProducerService.sendPerson("person-topic", person);
        return "Person sent successfully: " + person;
    }
}