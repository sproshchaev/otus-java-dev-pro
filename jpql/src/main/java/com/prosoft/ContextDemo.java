package com.prosoft;

import com.prosoft.entity.Category;
import com.prosoft.util.LiquibaseRunner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ContextDemo {

    public static final String PERSISTENCE_UNIT_NAME = "SingleUnit";

    /**
     * Перед повторным запуском необходимо выполнить запрос:
     * DELETE FROM categories WHERE name NOT IN ('Development', 'Architecture', 'DevOps', 'Analysis', 'Design');
     */
    public static void main(String[] args) {

        /** Запуск миграций Liquibase */
        LiquibaseRunner.runMigrations();

        /**  Демонстрация работы операции persist */
        persistDemo();

        /** Демонстрация работы операции flush */
        flushDemo();

        /** Демонстрация работы операций detach и merge */
        detachMergeDemo();

        /** Демонстрация работы операции remove */
        removeDemo();

    }

    /**
     * Метод persistDemo(). Демонстрация работы операции persist в JPA.
     * <p>
     * В этом методе показан процесс сохранения новой сущности в базе данных:
     * 1. Создается новая категория
     * 2. Начинается транзакция
     * 3. Новая сущность сохраняется в базе данных с помощью persist
     * 4. Производится поиск только что сохраненной сущности в контексте persistence
     * 5. Транзакция фиксируется
     * <p>
     * Особенности демонстрационного метода:
     * - Создается новая категория с именем "Testing"
     * - Сущность сохраняется в базе данных
     * - Производится поиск сущности через find
     * <p>
     * Результат в базе данных:
     * - Будет создана новая сущность с именем "Testing"
     * - Новая сущность будет иметь уникальный сгенерированный идентификатор
     */
    public static void persistDemo() {
        // Создание фабрики менеджеров сущностей для работы с persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        // Получение менеджера сущностей для работы с базой данных
        EntityManager entityManager = emf.createEntityManager();

        // Создание новой категории с именем "Testing"
        Category newCategory = new Category("Testing");

        // Начало транзакции
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Сохранение новой категории в базе данных: передаем entityManager нащ объект в методе persist
        // INSERT в БД не выполняется
        entityManager.persist(newCategory);

        // Поиск только что сохраненной сущности в контексте persistence
        // Объект (тот же самый) возвращается из Persistent Context - SELECT в БД не выполняется
        Category categoryFromContext = entityManager.find(Category.class, newCategory.getId());

        // Завершение транзакции и фиксация изменений в базе данных
        // INSERT в БД
        transaction.commit();

        // Закрытие менеджера сущностей
        entityManager.close();
    }


    /**
     * Метод flushDemo(). Демонстрация работы операции flush в JPA.
     * <p>
     * В этом методе показан процесс синхронизации состояния persistence контекста с базой данных:
     * 1. Создается новая категория
     * 2. Начинается транзакция
     * 3. Новая сущность сохраняется в базе данных с помощью persist
     * 4. Выполняется первый flush для немедленной синхронизации
     * 5. Изменяется имя сущности
     * 6. Выполняется второй flush для обновления базы данных
     * <p>
     * Особенности демонстрационного метода:
     * - Создается новая категория с initial именем "MANAGEMENT"
     * - После первого flush сущность сохраняется в базе
     * - После второго flush имя меняется на "Management"
     * <p>
     * Результат в базе данных:
     * - Будет создана новая сущность с именем "Management"
     * - Сущность будет иметь уникальный сгенерированный идентификатор
     */
    public static void flushDemo() {
        // Создание фабрики менеджеров сущностей для работы с persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        // Получение менеджера сущностей для работы с базой данных
        EntityManager entityManager = emf.createEntityManager();

        // Создание новой категории с initial именем "MANAGEMENT"
        Category newCategory = new Category("MANAGEMENT");

        // Начало транзакции
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Сохранение новой категории в persistence контексте
        // Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);

        // Первичная синхронизация состояния с базой данных
        // Выполняется синхронизация Persistent Context c БД - выполняется INSERT
        entityManager.flush();

        // Изменение имени сущности
        newCategory.setName("Management");

        // Повторная синхронизация с обновленным именем
        // Выполняется синхронизация Persistent Context с БД - выполняется UPDATE
        entityManager.flush();

        // Завершение транзакции и окончательная фиксация изменений
        // INSERT/UPDATE не выполняются (были выполнены ранее)
        transaction.commit();

        // Закрытие менеджера сущностей
        entityManager.close();
    }

    /**
     * Метод detachMergeDemo(). Демонстрация работы операций detach и merge в JPA.
     * <p>
     * В этом методе показан жизненный цикл сущности в контексте persistence:
     * 1. Создается новая категория и сохраняется в базе данных
     * 2. Сущность отсоединяется от контекста persistence с помощью detach
     * 3. Производится изменение отсоединенной сущности
     * 4. Сущность возвращается в контекст persistence с помощью merge
     * <p>
     * Особенности демонстрационного метода:
     * - Создается новая категория с именем "SUPPORT"
     * - После detach изменяется имя на "Support"
     * - После merge сущность сохраняется с именем "Support"
     * <p>
     * Результат в базе данных:
     * - Будет создана новая сущность с именем "Support"
     * - Изменение на "New Analysis" не попадет в базу данных
     */
    public static void detachMergeDemo() {
        // Создание фабрики менеджеров сущностей для работы с persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        // Получение менеджера сущностей для работы с базой данных
        EntityManager entityManager = emf.createEntityManager();

        // Создание новой категории с initial именем "SUPPORT"
        Category newCategory = new Category("SUPPORT");

        // Начало первой транзакции
        EntityTransaction transaction1 = entityManager.getTransaction();
        transaction1.begin();

        // Сохранение новой категории в контекст
        // Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);

        // Синхронизация состояния базы данных
        // Выполняется синхронизация Persistent Context c БД - выполняется INSERT
        entityManager.flush();

        // Отсоединение сущности от контекста persistence
        // Объект отключается от Persistence Context - синхронизации с БД останавливается
        entityManager.detach(newCategory);

        // Изменение имени отсоединенной сущности
        newCategory.setName("Support");

        // Завершение первой транзакции
        // Выполняется коммит транзакции 1 - UPDATE не выполняется (сущность изменена после отключения от контекста)
        transaction1.commit();

        // Начало второй транзакции
        EntityTransaction transaction2 = entityManager.getTransaction();
        transaction2.begin();

        // Возвращение сущности в контекст persistence с обновлением состояния
        // Объект повторно помещается к Persistence Context - выполняется SELECT из БД, создается новый объект,
        // состояние исходного объекта копируется объект, созданный при merge - синхронизация с БД
        // восстанавливается уже для нового объекта
        Category mergedCategory = entityManager.merge(newCategory);

        // Попытка изменения имени, которая не повлияет на базу данных
        newCategory.setName("New Analysis");

        // Завершение второй транзакции
        // Выполняется UPDATE со значением из объекта, созданного при merge
        // В БД появится новая запись с именем "Support"
        transaction2.commit();

        // Закрытие менеджера сущностей
        entityManager.close();
    }

    /**
     * Метод removeDemo(). Демонстрация работы операции remove в JPA.
     * <p>
     * В этом методе показан процесс удаления сущности из базы данных:
     * 1. Создается новая категория
     * 2. Начинается первая транзакция
     * 3. Новая сущность сохраняется в базе данных с помощью persist
     * 4. Первая транзакция фиксируется
     * 5. Начинается вторая транзакция
     * 6. Сущность удаляется из базы данных с помощью remove
     * <p>
     * Особенности демонстрационного метода:
     * - Создается новая категория с именем "Operations"
     * - Сущность сначала сохраняется в базе данных
     * - Затем сущность полностью удаляется из базы данных
     * <p>
     * Результат в базе данных:
     * - Сущность "Operations" будет создана и сразу же удалена
     * - После выполнения метода сущность отсутствует в базе данных
     */
    public static void removeDemo() {
        // Создание фабрики менеджеров сущностей для работы с persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

        // Получение менеджера сущностей для работы с базой данных
        EntityManager entityManager = emf.createEntityManager();

        // Создание новой категории с именем "Operations"
        Category newCategory = new Category("Operations");

        // Начало первой транзакции
        EntityTransaction transaction1 = entityManager.getTransaction();
        transaction1.begin();

        // Сохранение новой категории в базе данных
        // Объект помещается в Persistence Context - INSERT не выполняется
        entityManager.persist(newCategory);

        // Завершение первой транзакции и фиксация сохранения
        transaction1.commit();

        // Начало второй транзакции
        EntityTransaction transaction2 = entityManager.getTransaction();
        transaction2.begin();

        // Удаление сущности
        // Выполняется удаление объекта из контекста - DELETE не выполняется
        entityManager.remove(newCategory);

        // Завершение второй транзакции и фиксация удаления
        // Выполняется коммит транзакции 2 - выполняется DELETE
        transaction2.commit();

        // Закрытие менеджера сущностей
        entityManager.close();
    }
}
