package com.prosoft;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import com.prosoft.agreates.CategoryInfo;
import com.prosoft.entity.Category;
import com.prosoft.entity.Course;

import java.util.List;
import java.util.UUID;

public class JpqlDemo {
    public static void main(String[] args) {
        // simpleQueryDemo();
        // normalQueryDemo();
        // ? advancedQueryDemo();
        // executeUpdateDemo();
    }

    public static void simpleQueryDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

//        Найти все категории
        List<Category> categoryList = entityManager
                .createQuery("SELECT C FROM Category C", Category.class)
                .getResultList();

//        Найти категорию с определенным именем
//        Если не будет найдено ни одного объекта, будет NoResultException
//        Если будет найдено больше одного объекта, будет исключение
        Category category = entityManager
                .createQuery("SELECT C FROM Category C WHERE C.name =:name", Category.class)
                .setParameter("name", "Analysis")
                .getSingleResult();

//        Найти id категории с определенным именем
        UUID categoryId = entityManager
                .createQuery("SELECT C.id FROM Category C where C.name = :name", UUID.class)
                .setParameter("name", "Development")
                .getSingleResult();


//        Найти курсы с высокой стоимостью
        List<Course> expensiveCourseList = entityManager
                .createQuery("SELECT C FROM Course C WHERE C.cost > :level", Course.class)
                .setParameter("level", 150)
                .getResultList();

        entityManager.close();
    }

    public static void normalQueryDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

//        Найти количество всех категорий
        Long categoryCount = entityManager
                .createQuery("SELECT COUNT(C) FROM Category C", Long.class)
                .getSingleResult();

//        Найти курсы с одним названием категории - не используем JOIN
        List<Course> devOpsCourseList = entityManager
                .createQuery("SELECT C FROM Course C WHERE C.category.name = :name", Course.class)
                .setParameter("name", "DevOps")
                .getResultList();

//        Найти среднюю стоимость дорогих курсов по всему кроме дизайна
        Double averageCost = entityManager
                .createQuery("SELECT avg (C.cost) FROM Course C " +
                        " WHERE C.cost > :level AND C.category.name <> :name", Double.class)
                .setParameter("level", 100)
                .setParameter("name", "Design")
                .getSingleResult();

        entityManager.close();
    }

    public static void advancedQueryDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

//        Найти среднюю стоимость курсов в каждой группе, где она больше уровня
        List<CategoryInfo> categoryInfoList = entityManager
                .createQuery("SELECT new ru.otus.javaprojpql.agreates.CategoryInfo(C.category.name, avg(C.cost)) " +
                        " FROM Course C GROUP BY C.category.name HAVING avg (C.cost) >= :level", CategoryInfo.class)
                .setParameter("level", 200)
                .getResultList();


        entityManager.close();
    }

    public static void executeUpdateDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

//        Обновить стоимость всех курсов группы с определенным назнванием на определенный процент
//        Не будет работать, так как неявный join не поддерживается в этом случае
//        int updatedRowCount1 = entityManager
//                .createQuery("UPDATE Course C SET C.cost = C.cost * :rate WHERE C.category.name = :name")
//                .setParameter("rate", 2)
//                .setParameter("name", "Analysis")
//                .executeUpdate();

//        Обновить стоимость всех курсов группы с определенным назнванием на определенный процент
        int updatedRowCount2 = entityManager
                .createQuery("UPDATE Course C SET C.cost = C.cost * :rate " +
                        " WHERE C.category.id IN (SELECT Ct.id FROM Category Ct WHERE Ct.name = :name)")
                .setParameter("rate", 2)
                .setParameter("name", "Analysis")
                .executeUpdate();

        transaction.commit();

        entityManager.close();
    }
}
