package com.prosoft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Устаревшая версия DTO - НЕТ поля email!
 * Это создаст проблему десериализации при получении сообщения от producer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private String name;
    private int age;
    // Нет поля email - намеренно устаревшая модель!
}