package com.prosoft;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;


class ConnectionTest {

    @Test
    @SneakyThrows
    void dbServer_Connecting_Connected() {
        String url = "jdbc:hsqldb:mem:myDb";
        String user = "sa";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Assertions.assertFalse(connection.isClosed(), "Соединение должно быть открыто.");
            connection.close();
            Assertions.assertTrue(connection.isClosed(), "Соединение должно быть закрыто.");
        }
    }

}
