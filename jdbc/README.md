# Java Database Connectivity (JDBC)

1. Структура проекта:
```txt
jdbc
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.prosoft/
│   │   │       ├── DatabaseInitializer.java         // Инициализация базы данных
│   │   │       ├── PreparedStatementCache.java      // Кэш для подготовленных SQL-выражений
│   │   │       └── SimpleConnectionPool.java        // Реализация простого пула соединений
│   │   ├── resources/
│   │   │   ├── data.sql                              // Скрипт для наполнения базы данных
│   │   │   └── schema.sql                            // Скрипт для создания структуры базы данных
│   ├── test/
│   │   ├── java/
│   │   │   └── com.prosoft/
│   │   │       ├── ConnectionPoolHsqlDbTest.java    // Тесты пула соединений с HSQLDB
│   │   │       ├── ConnectionTest.java              // Тесты подключения к базе данных
│   │   │       ├── HikariPoolPgTest.java            // Тесты пула HikariCP с PostgreSQL
│   │   │       ├── PreparedStatementCacheTest.java  // Тесты для кэша подготовленных SQL-выражений
│   │   │       ├── ResultSetTest.java               // Тесты работы с результатами запросов
│   │   │       ├── SimpleConnectionPoolTest.java    // Тесты простого пула соединений
│   │   │       └── TransactionTest.java             // Тесты транзакционной обработки
│   │   └── resources/
│   └── build.gradle.kts                              // Скрипт сборки Gradle (Kotlin DSL)
│   └── docker-compose.yaml                           // Конфигурация Docker Compose для запуска окружения
│   └── README.md                                     // Описание проекта
```


### References 

1. Introduction to JDBC https://www.baeldung.com/java-jdbc

