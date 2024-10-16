package com.prosoft;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

class DatabaseInitializer {

    public static void initializeDatabase() {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "username";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Выполнение schema.sql
            String schema = readFileFromResources("schema.sql");
            stmt.execute(schema);

            // Выполнение data.sql
            String data = readFileFromResources("data.sql");
            stmt.execute(data);

            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для чтения файла из ресурсов
    private static String readFileFromResources(String fileName) throws Exception {
        try (InputStream inputStream = DatabaseInitializer.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}

