package com.prosoft;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import com.prosoft.entity.Category;

public class ContextDemo {
    public static void main(String[] args) {
        persistDemo();
//        flushDemo();
//        detachMergeDemo();
//        removeDemo();
    }

    public static void persistDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

        Category newCategory = new Category("Development4");

        EntityTransaction transaction = entityManager.getTransaction();
//        Выполняется открытие транзакции
        transaction.begin();
//        Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);
//        Объект (тот же самый) возвращается из Persistent Context - SELECT не выполняется
        Category categoryFromContext = entityManager.find(Category.class, newCategory.getId());
//        Выполняется коммит транзакции - выполняется INSERT
        transaction.commit();
        entityManager.close();
    }

    public static void flushDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

        Category newCategory = new Category("Architecture");

        EntityTransaction transaction = entityManager.getTransaction();
//        Выполняется открытие транзакции
        transaction.begin();
//        Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);
//        Выполняется синхронизация Persistent Context c БД - выполняется INSERT
        entityManager.flush();
        newCategory.setName("Design");
//        Выполняется синхронизация Persistent Context с БД - выполняется UPDATE
        entityManager.flush();
//        Выполняется коммит транзакции - INSERT/UPDATE не выполняются (были выполнены ранее)
        transaction.commit();
        entityManager.close();
    }

    public static void detachMergeDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

        Category newCategory = new Category("DevOps");

        EntityTransaction transaction1 = entityManager.getTransaction();
//        Выполняется открытие транзакции 1
        transaction1.begin();
//        Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);
//        Выполняется синхронизация Persistent Context c БД - выполняется INSERT
        entityManager.flush();
//        Объект отключается от Persistence Context - синхронизации с БД останавливается
        entityManager.detach(newCategory);
        newCategory.setName("Testing");
//        Выполняется коммит транзакции 1 - UPDATE не выполняется (сущность  изменена после  отключения от контекста)
        transaction1.commit();

        EntityTransaction transaction2 = entityManager.getTransaction();
//        Выполняется открытие транзакции 2
        transaction2.begin();
//        Объект повторно помещается к Persistence Context - выполняется SELECT из БД, создается новый объект,
//        состояние исходного объекта копируется объект, созданный при merge - синхронизация с БД
//        восстанавливается уже для нового объекта
        Category mergedCategory = entityManager.merge(newCategory);
        newCategory.setName("Analysis");
//        Выполняется коммит транзакции - выполняется UPDATE со значением из объекта, созданного при merge
        transaction2.commit();
        entityManager.close();
    }

    public static void removeDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SingleUnit");
        EntityManager entityManager = emf.createEntityManager();

        Category newCategory = new Category("Development");

        EntityTransaction transaction1 = entityManager.getTransaction();
//        Выполняется открытие транзакции 1
        transaction1.begin();
//        Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);
//        Выполняется коммит транзакции 1 - выполняется INSERT
        transaction1.commit();

        EntityTransaction transaction2 = entityManager.getTransaction();
//        Выполняется открытие транзакции 2
        transaction2.begin();
//        Выполняется удаление объекта из контекста - DELETE не выполняется
        entityManager.remove(newCategory);
//        Выполняется коммит транзакции 2 - выполняется DELETE
        transaction2.commit();
        entityManager.close();
    }
}
