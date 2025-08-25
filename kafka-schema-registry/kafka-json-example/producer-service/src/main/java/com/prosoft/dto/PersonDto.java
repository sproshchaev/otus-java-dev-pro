package com.prosoft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private String name;
    private int age;
    private String email; // <- Это поле будет вызывать проблему у consumer!
}