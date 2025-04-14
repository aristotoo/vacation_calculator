# Vacation Pay Calculator

## Описание
Сервис для расчета отпускных выплат на основе среднего заработка и периода отпуска. Сервис предоставляет REST API для выполнения расчетов.

## Технологии
- Java 11
- Spring Boot 2.7.x
- Maven
- OpenAPI 3.0 (Swagger)
- Lombok
- JUnit 5
- Mockito

## Требования
- JDK 11
- Maven 3.6 или выше

## Запуск приложения
1. Клонируйте репозиторий:
```bash
git clone https://github.com/aristotoo/vacation_calculator.git
cd vacation_calculator
```

2. Соберите проект:
```bash
mvn clean install
```

3. Запустите приложение:
```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

## API Документация
После запуска приложения, документация API доступна по следующим URL:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

## Использование API

### Расчет отпускных
```
GET /api/v1/calculate
```

#### Параметры запроса:
- `averageSalary` (обязательный) - средний заработок за 12 месяцев
- `vacationDaysCount` (опциональный) - количество дней отпуска
- `startVacation` (опциональный) - дата начала отпуска (формат: yyyy-MM-dd)
- `endVacation` (опциональный) - дата окончания отпуска (формат: yyyy-MM-dd)

**Примечание:** Необходимо указать либо `vacationDaysCount`, либо пару `startVacation` и `endVacation`.

#### Примеры запросов:

1. Расчет по количеству дней:
```
GET /api/v1/calculate?averageSalary=50000&vacationDaysCount=14
```

2. Расчет по периоду:
```
GET /api/v1/calculate?averageSalary=50000&startVacation=2024-01-01&endVacation=2024-01-14
```

#### Пример ответа:
```json
{
    "success": true,
    "message": "Vacation pay calculated successfully",
    "statusCode": 200,
    "status": "OK",
    "data": 19178.08
}
```

## Тестирование
Для запуска тестов выполните:
```bash
mvn test
```

## Структура проекта
```
src/
├── main/
│   ├── java/
│   │   └── com/bogdan/calculator/
|   |   |    ├── aop/
│   │   |    ├── config/       # Конфигурационные классы
│   │   |    ├── controller/   # REST контроллеры
│   │   |    ├── dto/          # Data Transfer Objects
│   │   |    ├── exception/    # Обработчики исключений
|   |   |    ├── mapper/       # Маппер сущностей
|   |   |    ├── model/        # Модель
|   |   |    ├── service/      # Бизнес-логика
│   │   |    └── validation/   # Валидатор
|   |   └── Main               # Точка входа в приложение
|   |
│   └── resources/
│       └── application.properties   # Конфигурация приложения
└── test/                     # Тесты
```

## Обработка ошибок
Сервис возвращает структурированные ответы об ошибках в формате:
```json
{
    "success": false,
    "message": "Описание ошибки",
    "statusCode": код_ошибки,
    "status": "статус",
    "data": null
}
```

# API request
Простой запрос:<br>
http://localhost:8080/api/v1/calculate?averageSalary=50000&vacationDaysCount=10 <br>
Доп.задание запрос c указанием точных дат:<br>
http://localhost:8080/api/v1/calculate?averageSalary=50000&startVacation=2025-05-01&endVacation=2025-05-11<br>