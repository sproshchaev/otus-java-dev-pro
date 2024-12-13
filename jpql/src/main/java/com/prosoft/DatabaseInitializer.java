package com.prosoft;

import com.prosoft.util.LiquibaseRunner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * DatabaseInitializer.
 * Запуск системы миграций, подготовка перед повторным запуском demo-методов, вывод состояния основных таблиц в консоль.
 */
public class DatabaseInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String ENTITY_PACKAGE = "com.prosoft.entity.";

    private DatabaseInitializer() {
    }

    public static void main(String[] args) {

        /** Запуск миграций Liquibase */
        LiquibaseRunner.runMigrations();
        LOGGER.info("LiquibaseRunner done.");

        /** Создание EntityManagerFactory для JPA - создаётся на уровне всего приложения и может использоваться многократно для создания различных EntityManager */
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SingleUnit");

        /** Создание EntityManager для работы с базой данных из EntityManagerFactory */
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            /** Открытие транзакции */
            entityManager.getTransaction().begin();

            /** Выполнение SQL-запроса для удаления данных */
            entityManager.createNativeQuery("DELETE FROM categories WHERE name NOT IN ('Development', 'Architecture', 'DevOps', 'Analysis', 'Design')")
                    .executeUpdate();
            LOGGER.info("Rows deleted from categories table.");

            logTableContents(entityManager, "Category");
            logTableContents(entityManager, "Contact");
            logTableContents(entityManager, "Course");
            logTableContents(entityManager, "Student");

            /** Завершение транзакции */
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.error("An error occurred while loading the entity class.", e);
        } finally {
            /** Закрытие EntityManagerFactory */
            entityManagerFactory.close();
        }
    }

    private static void logTableContents(EntityManager entityManager, String entityClassName) {
        try {
            Class<?> entityClass = Class.forName(ENTITY_PACKAGE + entityClassName);

            List<?> resultList = entityManager.createQuery("SELECT e FROM " + entityClassName + " e", entityClass).getResultList();

            LOGGER.info("Table '{}' contains {} records:", entityClassName, resultList.size());

            for (Object entity : resultList) {
                LOGGER.info("{}: {}", entityClassName, entity);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("Entity class not found: {}", entityClassName, e);
        }
    }
}
