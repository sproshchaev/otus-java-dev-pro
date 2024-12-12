# jpql

```txt
src/
 ├─ main/
 │   ├─ java/
 │   │   ├─ com/
 │   │       ├─ prosoft/
 │   │           ├─ HibernateApp.java
 │   │           ├─ LiquibaseRunner.java
 │   ├─ resources/     # Конфигурационные файлы
 │       ├─ db.changelog-master.yaml
```

Запуск pgAdmin из Docker 
```txt
1) Запустить контейнеры из docker-compose.yaml

2) Перейти в терминал и выполнить команду: docker network inspect jpql_db-network
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
