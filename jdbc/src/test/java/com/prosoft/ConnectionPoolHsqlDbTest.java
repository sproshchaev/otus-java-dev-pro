package com.prosoft;

import lombok.SneakyThrows;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.jupiter.api.Test;

import javax.naming.InitialContext;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * HSQLDB
 */
class ConnectionPoolHsqlDbTest {

    @Test
    @SneakyThrows
    void connectionPool_Connecting_Connected_HSQLDB() {
        // Создание источника данных HSQLDB
        JDBCDataSource dataSource = createDataSource();

        // Для встроенной базы данных HSQLDB в памяти, то JNDI-контекст не обязателен

        // Получение соединения и проверка
        try (Connection connection = dataSource.getConnection()) {
            // Проверка, что соединение не закрыто
            assertFalse(connection.isClosed(), "Соединение должно быть открыто!");
        }
    }

    /**
     * Метод для создания источника данных (пул соединений HSQLDB)
     */
    private JDBCDataSource createDataSource() {
        JDBCDataSource dataSource = new JDBCDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:myDb");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    // Метод для создания контекста JNDI
    @SneakyThrows
    private InitialContext createInitialContext() {
        return new InitialContext();
    }

}
