package com.prosoft;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для демонстрации использования PreparedStatement и транзакций в JDBC.
 * Используется PostgreSQL.
 */
class TransactionTest {

    private static Connection connection;

    @BeforeAll
    static void setUp() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "username";
        String password = "password";

        connection = DriverManager.getConnection(url, user, password);
        DatabaseInitializer.initializeDatabase(); // Инициализация базы данных
    }

    /**
     * Тест для демонстрации работы транзакции с PreparedStatement.
     */
    @Test
    void testTransactionWithPreparedStatement() throws Exception {
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        try {
            // Устанавливаем уровень изоляции транзакции
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            // Отключаем автокоммит для начала транзакции
            connection.setAutoCommit(false);

            // Вставляем новую запись
            String insertSQL = "INSERT INTO users (id, name, email, active) VALUES (?, ?, ?, ?)";
            insertStmt = connection.prepareStatement(insertSQL);
            insertStmt.setInt(1, 3);
            insertStmt.setString(2, "Charlie");
            insertStmt.setString(3, "charlie@example.com");
            insertStmt.setBoolean(4, true);
            insertStmt.executeUpdate();

            // Обновляем существующую запись
            String updateSQL = "UPDATE users SET active = ? WHERE email = ?";
            updateStmt = connection.prepareStatement(updateSQL);
            updateStmt.setBoolean(1, false); // Деактивируем пользователя
            updateStmt.setString(2, "alice@example.com");
            updateStmt.executeUpdate();

            // Фиксируем транзакцию
            connection.commit();

            // Проверяем, что изменения сохранены в базе данных
            String checkSQL = "SELECT * FROM users WHERE id = 3";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
                 ResultSet rs = checkStmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("Charlie", rs.getString("name"));
            }

            String checkUpdateSQL = "SELECT * FROM users WHERE email = 'alice@example.com'";
            try (PreparedStatement checkUpdateStmt = connection.prepareStatement(checkUpdateSQL);
                 ResultSet rs = checkUpdateStmt.executeQuery()) {
                assertTrue(rs.next());
                assertFalse(rs.getBoolean("active")); // Проверяем, что пользователь деактивирован
            }

        } catch (SQLException e) {
            // Откатываем транзакцию в случае ошибки
            connection.rollback();
            fail("Транзакция откатилась из-за ошибки: " + e.getMessage());
        } finally {
            if (insertStmt != null) insertStmt.close();
            if (updateStmt != null) updateStmt.close();
        }
    }

    /**
     * Тестируем работу транзакции с откатом.
     */
    @Test
    void testTransactionRollback() throws Exception {
        PreparedStatement insertStmt = null;

        try {
            // Устанавливаем уровень изоляции транзакции
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            // Отключаем автокоммит для начала транзакции
            connection.setAutoCommit(false);

            // Вставляем новую запись
            String insertSQL = "INSERT INTO users (id, name, email, active) VALUES (?, ?, ?, ?)";
            insertStmt = connection.prepareStatement(insertSQL);
            insertStmt.setInt(1, 4);
            insertStmt.setString(2, "David");
            insertStmt.setString(3, "david@example.com");
            insertStmt.setBoolean(4, true);
            insertStmt.executeUpdate();

            // Симулируем ошибку
            throw new SQLException("Искусственная ошибка для проверки отката транзакции.");

        } catch (SQLException e) {
            // Откатываем транзакцию в случае ошибки
            connection.rollback();

            // Проверяем, что данные не добавлены после отката
            String checkSQL = "SELECT * FROM users WHERE email = 'david@example.com'";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
                 ResultSet rs = checkStmt.executeQuery()) {
                assertFalse(rs.next()); // Данные не должны быть найдены
            }

        } finally {
            if (insertStmt != null) insertStmt.close();
        }
    }
}
