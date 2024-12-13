# Java Persistence Query Language (JPQL)

1. Структура проекта:
```txt
jpql
├── build/
│   └── (файлы сборки проекта)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.prosoft/
│   │   │       ├── aggregates/
│   │   │       │   └── CategoryInfo.java        // Модель для представления категорий
│   │   │       ├── entity/
│   │   │       │   ├── Category.java           // Сущность для категорий
│   │   │       │   ├── Contact.java            // Сущность для контактов
│   │   │       │   ├── ContactType.java        // Перечисление типов контактов
│   │   │       │   ├── Course.java             // Сущность для курсов
│   │   │       │   └── Student.java            // Сущность для студентов
│   │   │       └── util/
│   │   │           ├── LiquibaseRunner.java    // Запуск миграций Liquibase
│   │   │           ├── ContextDemo.java        // Демонстрация работы контекста
│   │   │           ├── DatabaseInitializer.java // Инициализация базы данных
│   │   │           ├── JpqlDemo.java           // Демонстрация JPQL-запросов
│   │   │           └── ProblemsDemo.java       // Кейсы и проблемы с JPQL
│   │   ├── resources/
│   │   │   ├── db.changelog/
│   │   │   │   ├── data/                       // Данные для заполнения
│   │   │   │   ├── structure/                  // Скрипты создания структуры
│   │   │   │   └── db.changelog-master.yaml    // Главный файл миграций Liquibase
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml             // Конфигурация JPA
│   │   │   └── logback.xml                     // Конфигурация логирования
│   └── test/
│       └── (тесты проекта)

```

2. Запуск pgAdmin из Docker 
```txt
1) Запустить контейнеры из docker-compose.yaml

2) Перейти в терминал и выполнить команду: 
                docker network inspect jpql_db-network
  где jpql - имя текущего проекта, где находится docker-compose.yaml 
      db-network - параметр из "networks:" в docker-compose.yaml

3) В результате выполнения команды в консоле найти значение IP-адреса "Gateway":
     "Config": [
                {
                    "Subnet": "172.18.0.0/16",
                    "Gateway": "172.18.0.1"
                }
            ] 

4) Перейти в браузере http://localhost:5050/browser/

5) Ввести: user@example.com
           admin

6) В секции "Quick Links" выбрать "Add New Server"

7) Ввести данные:
   "General"
     - Name:                 postgres-15   (из "container_name" в файле docker-compose.yaml)
   "Connection"
     - Host name/address:    172.18.0.1    (из "Gateway")
     - Port:                 5432          (из "ports: - "5432:..." в файле docker-compose.yaml)
     - Maintenance database: otus-db       (из "environment: POSTGRES_DB: otus-db" в файле docker-compose.yaml)  
     - Username:             user          (из "environment: POSTGRES_USER: ***" в файле docker-compose.yaml)
     - Password:             password      (из "environment: POSTGRES_PASSWORD: ***" в файле docker-compose.yaml)
   [Save]
```

### References 

1. Официальная документация Hibernate: [https://hibernate.org/orm/](https://hibernate.org/orm/)  

2. Основы Hibernate на русском языке: https://proselyte.net/tutorials/hibernate-tutorial/

3. Учебное пособие от JournalDev: [https://www.journaldev.com/2803/hibernate-tutorial-for-beginners](https://www.journaldev.com/2803/hibernate-tutorial-for-beginners)  

