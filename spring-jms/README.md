# spring-jms


```txt
Чтобы восстановить кэши в RabbitMQ при перезапуске контейнера, можно настроить постоянные тома (volumes) для хранения 
данных и состояния очередей. Таким образом, данные и состояние, включая кэш, будут сохраняться на хост-машине 
и восстанавливаться при перезапуске контейнера.
```
```yaml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq:3.13.3-management
    container_name: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq
      - rabbitmq_config:/etc/rabbitmq

volumes:
  rabbitmq_data:
  rabbitmq_config:
```
```txt
где: 
1) volumes в rabbitmq:
    - rabbitmq_data:/var/lib/rabbitmq — том для хранения данных RabbitMQ, таких как очереди и их состояния (в том числе кэш).
    - rabbitmq_config:/etc/rabbitmq — том для хранения конфигурации RabbitMQ, если требуется сохранять изменения в настройках.

2. volumes в docker-compose.yaml:
    - объявляются тома `rabbitmq_data` и `rabbitmq_config`, которые Docker будет использовать для хранения данных и конфигурации.

Когда перезапускается контейнер, RabbitMQ будет использовать эти тома для восстановления данных и кэша.
```