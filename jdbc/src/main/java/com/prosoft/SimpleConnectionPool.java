package com.prosoft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Простой пул соединений для управления подключениями к базе данных.
 */
public class SimpleConnectionPool {
    private String url;
    private String user;
    private String password;
    private int poolSize;
    private List<Connection> availableConnections = new ArrayList<>();
    private List<Connection> usedConnections = new ArrayList<>();

    public SimpleConnectionPool(String url, String user, String password, int poolSize) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
        this.poolSize = poolSize;

        for (int i = 0; i < poolSize; i++) {
            availableConnections.add(createConnection());
        }
    }

    /**
     * Создает новое соединение с базой данных.
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Получает соединение из пула.
     */
    public synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            throw new SQLException("No available connections");
        }

        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    /**
     * Возвращает соединение обратно в пул.
     */
    public synchronized void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    /**
     * Закрывает все соединения, когда пул больше не нужен.
     */
    public synchronized void shutdown() throws SQLException {
        for (Connection connection : usedConnections) {
            connection.close();
        }
        for (Connection connection : availableConnections) {
            connection.close();
        }
        availableConnections.clear();
        usedConnections.clear();
    }

    /**
     * Проверка размера пула соединений.
     */
    public int getAvailableConnectionCount() {
        return availableConnections.size();
    }

}
