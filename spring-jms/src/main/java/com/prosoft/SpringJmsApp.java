package com.prosoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJms
@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
public class SpringJmsApp {

    public static void main(String... args) {
        SpringApplication.run(SpringJmsApp.class, args);
    }

}
