package com.prosoft.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseRunner {

    private LiquibaseRunner() {
    }

    public static void runMigrations() {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/otus-db";
        String username = "user";
        String password = "password";
        String changelogFile = "db/changelog/db.changelog-master.yaml";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new liquibase.database.jvm.JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
            System.out.println("Liquibase migrations applied successfully.");
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException("Error while running Liquibase migrations", e);
        }
    }
}
