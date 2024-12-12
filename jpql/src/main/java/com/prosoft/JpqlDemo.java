package com.prosoft;

import com.prosoft.agreates.CategoryInfo;
import com.prosoft.entity.Category;
import com.prosoft.entity.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class JpqlDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpqlDemo.class);

    public static final String PERSISTENCE_UNIT_NAME = "SingleUnit";

    public static void main(String[] args) {
        simpleQueryDemo();
        normalQueryDemo();
        advancedQueryDemo();
        executeUpdateDemo();
    }

    /**
     * simpleQueryDemo()
     */
    public static void simpleQueryDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Найти все категории
        List<Category> categoryList = entityManager
                .createQuery("SELECT C FROM Category C", Category.class)
                .getResultList();
        categoryList.forEach(category -> LOGGER.info("{}", category));

        // Найти категорию с определенным именем
        // Если не будет найдено ни одного объекта, будет NoResultException
        // Если будет найдено больше одного объекта, будет исключение
        Category category = entityManager
                .createQuery("SELECT C FROM Category C WHERE C.name =:name", Category.class)
                .setParameter("name", "Analysis")
                .getSingleResult();
        LOGGER.info("Category: {}", category);

        // Найти id категории с определенным именем
        UUID categoryId = entityManager
                .createQuery("SELECT C.id FROM Category C where C.name = :name", UUID.class)
                .setParameter("name", "Development")
                .getSingleResult();
        LOGGER.info("Category ID for 'Development': {}", categoryId);

        // Найти курсы с высокой стоимостью
        List<Course> expensiveCourseList = entityManager
                .createQuery("SELECT C FROM Course C WHERE C.cost > :level", Course.class)
                .setParameter("level", 150)
                .getResultList();
        expensiveCourseList.forEach(course -> LOGGER.info("Expensive Course: {}", course));

        entityManager.close();
    }

    /**
     * normalQueryDemo()
     */
    public static void normalQueryDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Найти количество всех категорий
        Long categoryCount = entityManager
                .createQuery("SELECT COUNT(C) FROM Category C", Long.class)
                .getSingleResult();
        LOGGER.info("Total number of categories: {}", categoryCount);

        // Найти курсы с одним названием категории - не используем JOIN (JOIN сделает за нас Hibernate)
        List<Course> devOpsCourseList = entityManager
                .createQuery("SELECT C FROM Course C WHERE C.category.name = :name", Course.class)
                .setParameter("name", "DevOps")
                .getResultList();
        LOGGER.info("Courses for 'DevOps' category:");
        devOpsCourseList.forEach(course -> LOGGER.info("{}", course));

        // Найти среднюю стоимость дорогих курсов по всему кроме дизайна
        Double averageCost = entityManager
                .createQuery("SELECT avg (C.cost) FROM Course C " +
                        " WHERE C.cost > :level AND C.category.name <> :name", Double.class)
                .setParameter("level", 100)
                .setParameter("name", "Design")
                .getSingleResult();
        LOGGER.info("Average cost of expensive courses (excluding 'Design'): {}", averageCost);

        entityManager.close();
    }

    /**
     * advancedQueryDemo()
     */
    public static void advancedQueryDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Найти среднюю стоимость курсов в каждой группе, где она больше уровня
        List<CategoryInfo> categoryInfoList = entityManager
                .createQuery("SELECT new com.prosoft.agreates.CategoryInfo(C.category.name, avg(C.cost)) " +
                        " FROM Course C GROUP BY C.category.name HAVING avg (C.cost) >= :level", CategoryInfo.class)
                .setParameter("level", 200)
                .getResultList();
        categoryInfoList.forEach(categoryInfo -> LOGGER.info("CategoryInfo: {}", categoryInfo));

        entityManager.close();
    }

    /**
     * executeUpdateDemo()
     */
    public static void executeUpdateDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Обновить стоимость всех курсов группы с определенным названием на определенный процент
        int updatedRowCount = entityManager
                .createQuery("UPDATE Course C SET C.cost = C.cost * :rate " +
                        " WHERE C.category.id IN (SELECT Ct.id FROM Category Ct WHERE Ct.name = :name)")
                .setParameter("rate", 2)
                .setParameter("name", "Analysis")
                .executeUpdate();
        LOGGER.info("Number of updated rows: {}", updatedRowCount);

        transaction.commit();

        entityManager.close();
    }
}
