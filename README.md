# REST API для управления пользователями (CRUD)

## Обзор
Данный проект представляет собой простое **CRUD** приложение для управления пользователями через REST API. Приложение написано на **Kotlin** с использованием **Spring Boot** и **PostgreSQL**, а для контейнеризации используется **Docker** и **Docker Compose**.

## Возможности
- **Создание**, **получение**, **обновление** и **удаление** пользователей через REST API.
- Интеграция с базой данных **PostgreSQL**.
- Контейнеризация с использованием **Docker**.
- Конфигурация через переменные окружения.

## Архитектура
Проект использует стандартную многослойную архитектуру:

1. **Слой сущностей**: Определяет сущность `User`, которая отображается на таблицу `users` в базе данных.
2. **Слой репозиториев**: Использует интерфейс Spring Data JPA для работы с базой данных.
3. **Слой контроллеров**: Реализует REST API для выполнения операций над пользователями.

---

## Структура проекта
```plaintext
src/
├── main/
│   ├── kotlin/
│   │   └── ru/arsentiev/app/
│   │       ├── users/
│   │       │   ├── User.kt                # Сущность пользователя
│   │       │   ├── UserRepository.kt      # Репозиторий для работы с БД
│   │       │   └── UserController.kt      # REST контроллер
│   └── resources/
│       ├── application.properties         # Конфигурация приложения
Dockerfile                                  # Dockerfile для сборки образа
docker-compose.yml                          # Конфигурация Docker Compose
```

---

## Описание компонентов
### Сущность пользователя (Entity)
Сущность `User` описывает модель данных для таблицы `users` в базе данных. Для отображения на таблицу используется **JPA** с аннотациями.

### Репозиторий (Repository)
Интерфейс `UserRepository` расширяет интерфейс `CrudRepository`, предоставляя базовые операции для работы с базой данных.

### REST контроллер (Controller)
`UserController` предоставляет REST API с маршрутами для операций над пользователями:
- Получение всех пользователей.
- Получение пользователя по идентификатору.
- Создание нового пользователя.
- Обновление существующего пользователя.
- Удаление пользователя по идентификатору.

---

## Конфигурация приложения
Конфигурация приложения задаётся в файле `application.properties`:

```properties
spring.application.name=app
spring.datasource.url=${DB_URL}
spring.datasource.username=${PG_USER}
spring.datasource.password=${PG_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Переменные окружения задаются через `docker-compose.yml`.

---

## Dockerизация
### Dockerfile
Для контейнеризации приложения используется следующий `Dockerfile`:

1. Базовый образ — `openjdk:21-jdk-slim-buster`.
2. Копирование исходного кода в контейнер.
3. Сборка приложения с использованием Gradle.
4. Открытие порта 8080 для внешних подключений.
5. Запуск приложения.

### Docker Compose
Файл `docker-compose.yml` описывает два сервиса:
1. **kotlinapp** — сервис приложения на Kotlin.
2. **db** — сервис базы данных PostgreSQL.

Использование `volumes` позволяет сохранять данные базы данных даже после остановки или удаления контейнера. В данном случае используется том `data`, который монтируется в директорию `/var/lib/postgresql/data` внутри контейнера базы данных. Это обеспечивает сохранность данных между перезапусками контейнеров.

Файл `docker-compose.yml`:

```yaml
services:
  kotlinapp:
    container_name: kotlinapp
    image: srbob01/kotlinapp/latest
    build:
      context: .
      dockerfile: Dockerfile
      args:
        DB_URL: ${DB_URL}
        PG_USER: ${PG_USER}
        PG_PASSWORD: ${PG_PASSWORD}
    ports:
      - "8080:8080"
    environment:
      DB_URL: jdbc:postgresql://db:5432/postgres
      PG_USER: postgres
      PG_PASSWORD: 112004
    depends_on:
      - db

  db:
    container_name: db
    image: postgres:latest
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: 112004
      POSTGRES_DB: postgres
    volumes:
      - data:/var/lib/postgresql/data

volumes:
  data: {}
```


---

## Запуск приложения
### Предварительные требования
- Установленные Docker и Docker Compose.

### Инструкция по запуску
1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/srBob01/user-rest-app.git
   ```
2. Перейдите в папку, куда вы клонировали репозиторий, и выполните:
   ```bash
   cd app
   ```

3. Соберите и запустите контейнеры:
   ```bash
   docker-compose up --build
   ```

4. Приложение будет доступно по адресу `http://localhost:8080`.

### Доступные API маршруты
| Метод   | Маршрут            | Описание                     |
|---------|--------------------|------------------------------|
| `GET`   | `/api/users`       | Получить всех пользователей  |
| `GET`   | `/api/users/{id}`  | Получить пользователя по ID  |
| `POST`  | `/api/users`       | Создать нового пользователя  |
| `PUT`   | `/api/users/{id}`  | Обновить пользователя по ID  |
| `DELETE`| `/api/users/{id}`  | Удалить пользователя по ID   |

---

## Примеры запросов
### 1. Создание пользователя
**Запрос:**
```bash
POST /api/users
Content-Type: application/json

{
    "username": "john_doe",
    "email": "john.doe@example.com"
}
```
**Ответ:**
```json
{
    "id": 1,
    "username": "john_doe",
    "email": "john.doe@example.com"
}
```

### 2. Получение всех пользователей
**Запрос:**
```bash
GET /api/users
```
**Ответ:**
```json
[
    {
        "id": 1,
        "username": "john_doe",
        "email": "john.doe@example.com"
    }
]
```

### 3. Обновление пользователя
**Запрос:**
```bash
PUT /api/users/1
Content-Type: application/json

{
    "username": "john_updated",
    "email": "john.updated@example.com"
}
```
**Ответ:**
```json
{
    "id": 1,
    "username": "john_updated",
    "email": "john.updated@example.com"
}
```

### 4. Удаление пользователя
**Запрос:**
```bash
DELETE /api/users/1
```
**Ответ:**
```bash
HTTP 204 No Content
```