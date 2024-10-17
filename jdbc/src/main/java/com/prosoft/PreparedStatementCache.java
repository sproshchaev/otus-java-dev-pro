package com.prosoft;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Простая реализация кэша для PreparedStatement, которая хранит подготовленные запросы и их результаты на стороне приложения.
 */
public class PreparedStatementCache {

    // Кэш для хранения PreparedStatement
    private Map<String, PreparedStatement> cache = new HashMap<>();

    public PreparedStatement getPreparedStatement(Connection connection, String sql, String param) throws SQLException {
        String key = sql + param; // Создаем уникальный ключ для кэша
        // Проверка наличия PreparedStatement в кэше
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            // Если не найдено, создаем новый PreparedStatement и добавляем в кэш
            PreparedStatement pstmt = connection.prepareStatement(sql);
            cache.put(key, pstmt);
            return pstmt;
        }
    }
}
