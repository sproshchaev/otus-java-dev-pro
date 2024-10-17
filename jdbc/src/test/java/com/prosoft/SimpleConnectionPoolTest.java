package com.prosoft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса SimpleConnectionPool.
 */
class SimpleConnectionPoolTest {

    private SimpleConnectionPool pool;

    @BeforeEach
    void setUp() throws Exception {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "username";
        String password = "password";

        // Инициализация пула соединений с 5 соединениями перед каждым тестом
        pool = new SimpleConnectionPool(url, user, password, 5);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (pool != null) {
            // Закрываем пул соединений после каждого теста
            pool.shutdown();
        }
    }

    /**
     * Тест получения соединения из пула и проверки, что соединение не null.
     */
    @Test
    void testGetConnection() throws SQLException {
        Connection connection = pool.getConnection();
        assertNotNull(connection, "Соединение не должно быть null.");
        pool.releaseConnection(connection);
    }

    /**
     * Тест возвращения соединения обратно в пул.
     */
    @Test
    void testReleaseConnection() throws SQLException {
        Connection connection = pool.getConnection();
        assertNotNull(connection, "Соединение не должно быть null.");
        pool.releaseConnection(connection);
        assertEquals(5, pool.getAvailableConnectionCount(), "После возврата соединения в пул количество доступных должно быть 5.");
    }

    /**
     * Тест попытки получения всех соединений из пула.
     */
    @Test
    void testExhaustConnectionPool() throws SQLException {
        // Получаем все соединения из пула
        for (int i = 0; i < 5; i++) {
            Connection connection = pool.getConnection();
            assertNotNull(connection, "Соединение не должно быть null.");
        }

        // Теперь пул пуст, и попытка взять еще одно соединение должна выбросить исключение
        assertThrows(SQLException.class, () -> pool.getConnection(), "Должно быть выброшено исключение, так как все соединения уже заняты.");
    }

    /**
     * Тест закрытия пула соединений.
     */
    @Test
    void testShutdown() throws SQLException {
        pool.shutdown();
        assertEquals(0, pool.getAvailableConnectionCount(), "После закрытия пула количество доступных соединений должно быть 0.");
    }

    /**
     * Тест на получение соединений и возврат их в пул с последующей проверкой доступных соединений.
     */
    @Test
    void testGetAndReleaseMultipleConnections() throws SQLException {
        Connection conn1 = pool.getConnection();
        Connection conn2 = pool.getConnection();

        assertNotSame(conn1, conn2, "Соединения должны быть разными.");
        assertEquals(3, pool.getAvailableConnectionCount(), "Должно остаться 3 соединения в пуле после того, как два были заняты.");

        pool.releaseConnection(conn1);
        pool.releaseConnection(conn2);

        assertEquals(5, pool.getAvailableConnectionCount(), "После возврата всех соединений в пул их количество должно быть 5.");
    }

    /**
     * Тест получения соединения из пула и выполнения SQL-запроса SELECT * FROM users.
     */
    @Test
    void testSelectFromUsers() throws SQLException {
        // Получаем соединение из пула
        Connection connection = pool.getConnection();
        assertNotNull(connection, "Соединение не должно быть null.");

        try (Statement statement = connection.createStatement()) {
            // Выполняем SQL-запрос
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            // Проверяем, что результат не пустой
            assertNotNull(resultSet, "Результат запроса не должен быть null.");

            // Проверяем, что хотя бы одна запись существует
            assertTrue(resultSet.next(), "Таблица users должна содержать хотя бы одну запись.");

            // Пример: Проверка данных первой строки
            String name = resultSet.getString("name");
            assertNotNull(name, "Имя пользователя не должно быть null.");
            System.out.println("Name: " + name);
        } finally {
            // Возвращаем соединение обратно в пул
            pool.releaseConnection(connection);
        }

        // Проверяем, что после возврата соединения в пул количество доступных соединений снова 5
        assertEquals(5, pool.getAvailableConnectionCount(), "После возврата соединения в пул количество доступных должно быть 5.");
    }
}
