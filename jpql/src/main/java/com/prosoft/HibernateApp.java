package com.prosoft;

import com.prosoft.util.LiquibaseRunner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Пример использования Hibernate через общий API (JPA):
 * - persistence.xml
 */
public class HibernateApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateApp.class);

    public static void main(String[] args) {

        /** Запуск миграций Liquibase */
        LiquibaseRunner.runMigrations();

        /** Создание EntityManagerFactory для JPA - создаётся на уровне всего приложения и может использоваться многократно для создания различных EntityManager */
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("SingleUnit");

        /** Создание EntityManager для работы с базой данных из EntityManagerFactory */
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            /** Открытие транзакции */
            entityManager.getTransaction().begin();

            /** Завершение транзакции */
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /** Закрытие EntityManagerFactory */
            entityManagerFactory.close();
        }
    }
}
