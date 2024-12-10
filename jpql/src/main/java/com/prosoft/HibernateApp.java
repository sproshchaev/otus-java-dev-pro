package com.prosoft;

import com.prosoft.domain.Person;
import com.prosoft.util.LiquibaseRunner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Пример использования Hibernate через общий API (JPA):
 * - persistence.xml
 */
public class HibernateApp {

    private static final Logger logger = LoggerFactory.getLogger(HibernateApp.class);

    public static void main(String[] args) {

        /** Запуск миграций Liquibase */
        LiquibaseRunner.runMigrations();

        /** Создание EntityManagerFactory для JPA - создаётся на уровне всего приложения и может использоваться многократно для создания различных EntityManager */
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-unit");

        /** Создание EntityManager для работы с базой данных из EntityManagerFactory */
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            /** Открытие транзакции */
            entityManager.getTransaction().begin();

            /** Выполнение запроса для получения всех сущностей Person */
            TypedQuery<Person> query = entityManager.createQuery("FROM Person", Person.class);
            List<Person> persons = query.getResultList();

            persons.forEach(person -> {
                logger.info("Person: " + person.getName());
                person.getPhones().forEach(phone ->
                        logger.info("  Phone: " + phone.getNumber())
                );
            });

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
