package com.prosoft;

import com.prosoft.domain.Person;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringJmsApp implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SpringJmsApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Отправка сообщения при старте приложения
        Person person = new Person("John Doe", 30);

        // Отправляем объект в RabbitMQ как сообщение
        rabbitTemplate.convertAndSend("personRoute", person);

        System.out.println("Message sent: " + person);

    }

}
