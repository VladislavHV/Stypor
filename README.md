# Drone Kafka Service

Spring Boot микросервис для приема данных о дронах через REST API и отправки в Apache Kafka.

## Функциональность

- **REST API** эндпоинт для приема данных о дронах
- **Валидация** входящих данных
- **Интеграция с Kafka** - отправка сообщений в топик
- **Обработка ошибок** с понятными сообщениями
- **Unit-тесты** с полным покрытием

## Требования

- Java 17+
- Apache Kafka
- Maven 3.6+

## Установка и запуск

### 1. Клонирование репозитория
```bash
git clone <repository-url>
cd drone-kafka-service

## Запуск Kafka через Docker
docker run -d --name kafka -p 9092:9092 \
  -e KAFKA_CFG_NODE_ID=0 \
  -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
  -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092 \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  apache/kafka:3.7.0

## Сборка и запуск приложения
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar

Maven
mvn spring-boot:run

## Тестирование API
curl -X POST http://localhost:8080/api/v1/drones/message \
  -H "Content-Type: application/json" \
  -d '{
    "model": "DJI Mavic 3",
    "speed": 15.5,
    "latitude": 55.7558,
    "longitude": 37.6173,
    "altitude": 120.0,
    "detectedBy": "Radar Station Alpha"
  }'
## Ответ
{
  "status": "success",
  "message": "Drone message sent to Kafka successfully"
}

## Тестирование ошибок
curl -X POST http://localhost:8080/api/v1/drones/message \
  -H "Content-Type: application/json" \
  -d '{
    "model": "",
    "speed": -5,
    "latitude": 200,
    "longitude": 200,
    "altitude": -10,
    "detectedBy": ""
  }'

## Запуск всех тестов
mvn test

## Структура проекта
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/     # REST контроллеры
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── config/        # Конфигурация Kafka
│   │   └── service/       # Бизнес-логика
│   └── resources/         # Конфигурационные файлы
└── test/                  # Unit-тесты

## Технологии
Spring Boot 3.5.6
Apache Kafka
Spring Validation
JUnit 5
Maven

## API Документация
{
  "model": "string (обязательно, макс. 100 символов)",
  "speed": "number (обязательно, > 0)",
  "latitude": "number (обязательно, -90 до 90)",
  "longitude": "number (обязательно, -180 до 180)", 
  "altitude": "number (обязательно, >= 0)",
  "detectedBy": "string (обязательно)"
}
