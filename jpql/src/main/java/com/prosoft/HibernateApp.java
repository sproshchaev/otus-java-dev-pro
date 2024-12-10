package com.prosoft;

import com.prosoft.domain.Person;
import com.prosoft.util.LiquibaseRunner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class HibernateApp {

    public static void main(String[] args) {

        // Запуск миграций с помощью Liquibase
        LiquibaseRunner.runMigrations();

        // Создание EntityManagerFactory для JPA
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-unit");

        // Создание EntityManager для работы с базой данных
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            // Открытие транзакции
            entityManager.getTransaction().begin();

            // Выполнение запроса для получения всех сущностей Person
            TypedQuery<Person> query = entityManager.createQuery("FROM Person", Person.class);
            List<Person> persons = query.getResultList();

            // Выводим полученные данные
            persons.forEach(person -> {
                System.out.println("Person: " + person.getName());
                person.getPhones().forEach(phone ->
                        System.out.println("  Phone: " + phone.getNumber())
                );
            });

            // Завершаем транзакцию
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Закрытие EntityManagerFactory
            entityManagerFactory.close();
        }
    }
}
