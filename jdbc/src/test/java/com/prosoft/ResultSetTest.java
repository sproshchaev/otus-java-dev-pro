package com.prosoft;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class ResultSetTest {

    private static Connection connection;

    @BeforeAll
    static void setUp() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "username";
        String password = "password";

        connection = DriverManager.getConnection(url, user, password);
        DatabaseInitializer.initializeDatabase(); // Инициализация базы данных
    }

    @Test
    void testNext() throws Exception {
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next()); // Проверяем, что есть первая запись
            assertEquals("Alice", rs.getString("name")); // Проверяем имя
            assertTrue(rs.next()); // Переходим к следующей записи
            assertEquals("Bob", rs.getString("name")); // Проверяем имя
        }
    }

    @Test
    void testGetBoolean() throws Exception {
        // Пример, если бы у нас была булевая колонка
        // assertFalse(rs.getBoolean("someBooleanColumn"));
    }

    @Test
    void testGetLong() throws Exception {
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next()); // Проверяем, что есть запись
            Long id = rs.getLong("id");
            assertNotNull(id); // Проверяем, что значение не null
            assertTrue(id instanceof Long); // Проверяем, что значение имеет тип Long

            assertTrue(rs.next()); // Переходим к следующей записи
            id = rs.getLong("id");
            assertNotNull(id); // Проверяем, что значение не null
            assertTrue(id instanceof Long); // Проверяем, что значение имеет тип Long
        }
    }

    @Test
    void testAbsolute() throws Exception {
        // SQL запрос для выбора всех пользователей
        String query = "SELECT * FROM users";

        // Создаем scrollable ResultSet, устанавливая соответствующие параметры
        try (PreparedStatement pstmt = connection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE, // Позволяет прокручивать ResultSet независимо от изменений в базе данных
                ResultSet.CONCUR_READ_ONLY); // Устанавливаем режим только для чтения
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.absolute(1)); // Переход к первой записи
            assertEquals("Alice", rs.getString("name")); // Проверяем, что имя соответствует ожидаемому значению "Alice"

            assertTrue(rs.absolute(2)); // Переход ко второй записи
            assertEquals("Bob", rs.getString("name")); // Проверяем, что имя соответствует ожидаемому значению "Bob"
        }
    }

    @Test
    void testUpdateString() throws Exception {
        // SQL запрос для выбора пользователя по email
        String query = "SELECT * FROM users WHERE email = ?";

        // Создаем PreparedStatement с заданным типом ResultSet и режимом параллелизма
        try (PreparedStatement pstmt = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            pstmt.setString(1, "alice@example.com"); // Устанавливаем параметр email

            // Выполняем запрос и получаем ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next()); // Проверяем, что есть хотя бы одна запись
                rs.updateString("name", "Alice Updated"); // Обновляем имя пользователя
                rs.updateRow(); // Обновляем запись в базе данных

                // Проверяем, что обновление прошло успешно
                assertEquals("Alice Updated", rs.getString("name")); // Сравниваем новое имя с ожидаемым значением
            }
        }
    }

    @Test
    void testMoveToInsertRow() throws Exception {
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = connection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = pstmt.executeQuery()) {

            rs.moveToInsertRow();
            rs.updateString("name", "Charlie");
            rs.updateString("email", "charlie@example.com");
            rs.insertRow(); // Вставляем запись

            // Проверяем, что запись была добавлена
            String checkQuery = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement checkPstmt = connection.prepareStatement(checkQuery)) {
                checkPstmt.setString(1, "charlie@example.com");
                try (ResultSet checkRs = checkPstmt.executeQuery()) {
                    assertTrue(checkRs.next());
                    assertEquals("Charlie", checkRs.getString("name")); // Проверяем имя вставленной записи
                }
            }
        }
    }

    @Test
    void testMoveToCurrentRow() throws Exception {
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = connection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next());
            String originalName = rs.getString("name");
            rs.moveToCurrentRow(); // Возвращаемся к текущей записи
            assertEquals(originalName, rs.getString("name")); // Проверяем имя
        }
    }

    @Test
    void testInsertRow() throws Exception {
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = connection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = pstmt.executeQuery()) {

            rs.moveToInsertRow();
            rs.updateString("name", "David");
            rs.updateString("email", "david@example.com");
            rs.insertRow(); // Вставляем запись

            // Проверяем, что запись была добавлена
            String checkQuery = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement checkPstmt = connection.prepareStatement(checkQuery)) {
                checkPstmt.setString(1, "david@example.com");
                try (ResultSet checkRs = checkPstmt.executeQuery()) {
                    assertTrue(checkRs.next());
                    assertEquals("David", checkRs.getString("name")); // Проверяем имя
                }
            }
        }
    }
}
