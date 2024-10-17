package com.prosoft;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Пул соединений Hikari.
 * Используется PostgreSQL.
 */
class HikariPoolPgTest {

    /**
     * Пример с реализацией Hikari.
     */
    @Test
    @SneakyThrows
    void hikariConnectionPool_Connecting_Connected() {
        HikariConfig configuration = new HikariConfig();

        configuration.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        configuration.setUsername("username");
        configuration.setPassword("password");

        HikariDataSource dataSource = new HikariDataSource(configuration);

        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
    }

}
