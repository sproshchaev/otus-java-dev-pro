package com.prosoft;

import com.prosoft.entity.Category;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ProblemsDemo
 */
public class ProblemsDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProblemsDemo.class);

    public static final String PERSISTENCE_UNIT_NAME = "SingleUnit";

    public static void main(String[] args) {

        // lazyInitializationExceptionDemo();

        // nPlusOneProblemDemo();

        // fetchSolutionDemo();

        entityGraphSolutionDemo();
    }

    /**
     * Метод lazyInitializationExceptionDemo().
     * Демонстрирует возникновение LazyInitializationException при попытке доступа
     * к лениво загружаемым данным после закрытия EntityManager.
     */
    public static void lazyInitializationExceptionDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Найти все категории (N) - курсы сразу не загружаются
        List<Category> categoryList = entityManager
                .createQuery("SELECT C FROM Category C", Category.class)
                .getResultList();

        entityManager.close();

        // При обращении к курсам за рамками сеанса получаем LazyInitializationException
        // Можно решить установкой fetch = FetchType.EAGER в @OneToMany
        for (Category category : categoryList) {
            LOGGER.info("{}", category.getCourseSet().size());
        }
    }


    /**
     * Метод nPlusOneProblemDemo().
     * Демонстрирует проблему N+1 при выполнении ленивой загрузки связанных данных в JPA.
     */
    public static void nPlusOneProblemDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Найти все категории (N) - курсы сразу не загружаются
        List<Category> categoryList = entityManager
                .createQuery("SELECT C FROM Category C", Category.class)
                .getResultList();

        // При обращении к курсам в течении сеанса происходит "ленивая" загрузка курсов по каждой категории отдельно
        // Можно решить проблему с использованием @Fetch(FetchMode.SUBSELECT) рядом с @OneToMany - не решает LazyInitializationException
        for (Category category : categoryList) {
            LOGGER.info("{}", category.getCourseSet().size());
        }

        entityManager.close();
    }

    /**
     * Метод fetchSolutionDemo().
     * Демонстрирует решение проблемы ленивой загрузки данных с использованием оператора {@code FETCH} в JPQL.
     */
    public static void fetchSolutionDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Найти все категории (N) - курсы загружаются сразу
        List<Category> categoryList = entityManager
                .createQuery("SELECT C FROM Category C LEFT JOIN FETCH C.courseSet", Category.class)
                .getResultList();

        entityManager.close();

        // При обращении к курсам не требуется дополнительная загрузка
        for (Category category : categoryList) {
            LOGGER.info("{}", category.getCourseSet().size());
        }
    }

    /**
     * Метод entityGraphSolutionDemo().
     * Демонстрирует использование EntityGraph для решения проблемы ленивой загрузки и предотвращения LazyInitializationException.
     */
    public static void entityGraphSolutionDemo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager entityManager = emf.createEntityManager();

        // Создаем указание на необходимость загружать именованный entity-граф - решает проблему LazyInitializationException
        EntityGraph entityGraph = entityManager.getEntityGraph("category-entity-graph");

        // Найти все категории (N) - курсы загружаются сразу
        List<Category> categoryList = entityManager
                .createQuery("SELECT C FROM Category C", Category.class)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();

        entityManager.close();

        // При обращении к курсам не требуется дополнительная загрузка
        for (Category category : categoryList) {
            LOGGER.info("{}", category.getCourseSet().size());
        }
    }
}

