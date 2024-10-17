package com.prosoft;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для кэширования PreparedStatement.
 */
class PreparedStatementCacheTest {

    private static Connection connection;
    private static PreparedStatementCache cache;

    @BeforeAll
    static void setUp() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "username";
        String password = "password";

        connection = DriverManager.getConnection(url, user, password);
        DatabaseInitializer.initializeDatabase(); // Инициализация базы данных
        cache = new PreparedStatementCache(); // Инициализация кэша
    }

    /**
     * Тестируем, что кэш работает корректно для одного и того же запроса.
     */
    @Test
    void testCacheFunctionality() throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";

        // Первая попытка - запрос к базе данных
        PreparedStatement pstmt1 = cache.getPreparedStatement(connection, sql, "alice@example.com");
        pstmt1.setString(1, "alice@example.com");
        try (ResultSet rs1 = pstmt1.executeQuery()) {
            assertTrue(rs1.next());
            assertEquals("Alice", rs1.getString("name")); // Проверяем имя
        }

        // Вторая попытка - должно использовать кэш
        PreparedStatement pstmt2 = cache.getPreparedStatement(connection, sql, "alice@example.com");
        pstmt2.setString(1, "alice@example.com");
        try (ResultSet rs2 = pstmt2.executeQuery()) {
            assertTrue(rs2.next());
            assertEquals("Alice", rs2.getString("name")); // Проверяем имя
        }

        // Проверяем, что оба PreparedStatement ссылаются на один и тот же объект
        assertSame(pstmt1, pstmt2);
    }

    /**
     * Тестируем кэширование с различными параметрами.
     */
    @Test
    void testDifferentParametersCache() throws Exception {
        String sql = "SELECT * FROM users WHERE email = ?";

        // Первый запрос с одним параметром
        PreparedStatement pstmt1 = cache.getPreparedStatement(connection, sql, "alice@example.com");
        pstmt1.setString(1, "alice@example.com");
        try (ResultSet rs1 = pstmt1.executeQuery()) {
            assertTrue(rs1.next());
            assertEquals("Alice", rs1.getString("name")); // Проверяем имя
        }

        // Второй запрос с другим параметром
        PreparedStatement pstmt2 = cache.getPreparedStatement(connection, sql, "bob@example.com");
        pstmt2.setString(1, "bob@example.com");
        try (ResultSet rs2 = pstmt2.executeQuery()) {
            assertTrue(rs2.next());
            assertEquals("Bob", rs2.getString("name")); // Проверяем имя
        }

        // Проверяем, что оба PreparedStatement не ссылаются на один и тот же объект (разные параметры)
        assertNotSame(pstmt1, pstmt2);
    }

    /**
     * Тестируем, что кэш работает корректно при различных запросах.
     */
    @Test
    void testDifferentQueriesCache() throws Exception {
        String sql1 = "SELECT * FROM users WHERE email = ?";
        String sql2 = "SELECT * FROM users WHERE id = ?";

        // Первый запрос
        PreparedStatement pstmt1 = cache.getPreparedStatement(connection, sql1, "alice@example.com");
        pstmt1.setString(1, "alice@example.com");
        try (ResultSet rs1 = pstmt1.executeQuery()) {
            assertTrue(rs1.next());
            assertEquals("Alice", rs1.getString("name")); // Проверяем имя
        }

        // Второй запрос
        PreparedStatement pstmt2 = cache.getPreparedStatement(connection, sql2, "2");
        pstmt2.setLong(1, 2); // Предполагаем, что ID Боба равен 2
        try (ResultSet rs2 = pstmt2.executeQuery()) {
            assertTrue(rs2.next());
            assertEquals("Bob", rs2.getString("name")); // Проверяем имя
        }

        // Проверяем, что оба PreparedStatement не ссылаются на один и тот же объект (разные запросы)
        assertNotSame(pstmt1, pstmt2);
    }
}
