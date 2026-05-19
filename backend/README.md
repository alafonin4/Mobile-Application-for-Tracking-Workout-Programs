# Backend

Backend-часть проекта представляет собой набор Spring Boot микросервисов, объединённых через Eureka и API Gateway. Эта часть отвечает за аутентификацию, профили пользователей, социальные функции, тренировки, программы, аналитику прогресса, персонализацию и уведомления.

## Состав backend

В каталоге [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend) находятся следующие сервисы:

- `discovery-server` - сервис регистрации и обнаружения микросервисов
- `api-gateway` - единая точка входа для мобильного клиента
- `auth-service` - регистрация, вход, JWT, смена пароля, удаление аккаунта
- `user-service` - профиль пользователя, аватар, базовые публичные данные
- `social-service` - друзья, заявки, соревнования, социальная персонализация, уведомления
- `workout-service` - тренировки, тренировочные программы, упражнения, избранное, прогресс и адаптация

## Архитектура backend

```text
                 +--------------------+
                 |  discovery-server  |
                 |       :8761        |
                 +---------+----------+
                           |
        +------------------+------------------+
        |                  |                  |
        v                  v                  v
  +-----------+      +-----------+      +-----------+
  | auth      |      | user      |      | social    |
  | :8082     |      | :8084     |      | :8085     |
  +-----------+      +-----------+      +-----------+
        |                  |                  |
        +------------------+------------------+
                           |
                           v
                    +-------------+
                    | workout     |
                    | :8086       |
                    +-------------+

Мобильный клиент -> api-gateway :8083 -> бизнес-сервисы
```

Каждый бизнес-сервис использует собственную PostgreSQL-базу данных. Внешние запросы от клиента должны идти через `api-gateway`, а внутренние вызовы между сервисами выполняются через service discovery.

## Ответственность сервисов

### `discovery-server`

- регистрация микросервисов в системе
- обнаружение сервисов по имени
- инфраструктурная зависимость для `api-gateway` и межсервисных вызовов

### `api-gateway`

- проксирование клиентских HTTP-запросов
- централизованное описание маршрутов
- единая входная точка для мобильного приложения

Маршруты описаны в [api-gateway/src/main/resources/application.properties](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/src/main/resources/application.properties).

### `auth-service`

- регистрация пользователя
- вход пользователя
- генерация и валидация JWT
- получение `user-info` по токену
- смена пароля
- удаление аккаунта

Основные контроллеры:

- [AuthController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/auth-service/src/main/java/ru/alafonin4/authserver/controllers/AuthController.java)
- [TokenController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/auth-service/src/main/java/ru/alafonin4/authserver/controllers/TokenController.java)

### `user-service`

- создание профиля после регистрации
- получение профиля по `id`
- обновление профиля
- удаление профиля
- получение списка всех пользователей
- хранение `avatarUrl` и публичных полей профиля

Основной контроллер:

- [UserController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/user-service/src/main/java/ru/alafonin4/userservice/controller/UserController.java)

### `social-service`

- отправка заявок в друзья
- принятие, отмена, отклонение и удаление из друзей
- определение отношений между пользователями
- общие соревнования
- пользовательские соревнования
- приглашения в соревнования
- ленты уведомлений и социальные инсайты

Основные контроллеры:

- [FriendRequestController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/src/main/java/ru/alafonin4/socialservice/controllers/FriendRequestController.java)
- [CompetitionController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/src/main/java/ru/alafonin4/socialservice/controllers/CompetitionController.java)
- [SocialInsightsController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/src/main/java/ru/alafonin4/socialservice/controllers/SocialInsightsController.java)

### `workout-service`

- CRUD тренировок
- CRUD тренировочных программ
- каталог упражнений
- избранные упражнения пользователя
- прогресс по пользователю
- прогресс по упражнению
- прогресс по диапазону дат
- персонализация, достижения, адаптация программы

Основные контроллеры:

- [WorkoutController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/WorkoutController.java)
- [TrainingProgramController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/TrainingProgramController.java)
- [ExerciseController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/ExerciseController.java)
- [PersonalizationController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/PersonalizationController.java)

## Базы данных

Backend ожидает наличие четырёх PostgreSQL-баз:

- `auth`
- `users`
- `social`
- `workout`

В Docker-сценарии для каждой базы уже предусмотрен отдельный контейнер. При локальном запуске без Docker их нужно создать вручную.

## Конфигурация

### Docker-переменные

Пример переменных окружения находится в [backend/.env.example](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/.env.example).

Основные параметры:

- `POSTGRES_USER`
- `AUTH_DB_PASSWORD`
- `USER_DB_PASSWORD`
- `SOCIAL_DB_PASSWORD`
- `WORKOUT_DB_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `SPRING_JPA_SHOW_SQL`
- `LOGGING_LEVEL_ROOT`
- `LOGGING_LEVEL_GATEWAY`
- `LOGGING_LEVEL_GATEWAY_ROUTES`

### Локальные `application.properties`

Если сервисы запускаются без Docker, datasource и Eureka можно переопределять через стандартные Spring Boot свойства или правкой `application.properties` внутри конкретного сервиса.

## Локальный запуск

Из каталога [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
mvn spring-boot:run -pl discovery-server
mvn spring-boot:run -pl api-gateway
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl social-service
mvn spring-boot:run -pl workout-service
```

Рекомендуемый порядок:

1. `discovery-server`
2. `api-gateway`
3. `auth-service`
4. `user-service`
5. `social-service`
6. `workout-service`

## Запуск через Docker Compose

Из каталога [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
docker compose up --build -d
```

Файл оркестрации:

- [docker-compose.yml](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/docker-compose.yml)

Compose-стек поднимает:

- все микросервисы
- все PostgreSQL-базы
- сетевое окружение между контейнерами

## Основные группы endpoint'ов

Через `api-gateway` доступны следующие основные группы маршрутов:

- `/api/auth/*`
- `/token/*`
- `/api/users/*`
- `/api/friendRequests/*`
- `/api/competitions/*`
- `/api/workouts/*`
- `/api/training-programs/*`
- `/api/exercises/*`
- `/api/personalization/*`
- `/api/social-personalization/*`
- `/api/notifications/*`

Если нужно посмотреть точные пути и HTTP-методы, их удобнее всего смотреть в двух местах:

- [api-gateway application.properties](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/src/main/resources/application.properties)
- конкретные `Controller`-классы каждого сервиса

## Сборка и тесты

Из каталога [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
mvn test
```

Также можно запускать отдельный модуль:

```bash
mvn test -pl workout-service
```

Или:

```bash
mvn spring-boot:run -pl auth-service
```

## Что стоит проверить вручную

После запуска backend полезно проверить такие сценарии:

1. Регистрация и логин пользователя
2. Получение `user-info` по токену
3. Создание и обновление профиля
4. Создание тренировки и получение тренировок по `userId`
5. Создание и редактирование тренировочной программы
6. Получение каталога упражнений и избранных упражнений
7. Отправка и принятие заявки в друзья
8. Получение общих и пользовательских соревнований
9. Получение прогресса и персонализации

## Примечания

- Система ориентирована на запуск нескольких независимых сервисов, поэтому при отладке важно следить не только за ошибкой конкретного модуля, но и за доступностью `Eureka`, `Gateway` и зависимых БД.
- Некоторые frontend-сценарии жёстко завязаны на gateway-маршруты, поэтому при добавлении нового endpoint его обычно нужно явно прописывать в `api-gateway`.
- Для backend-модулей в репозитории уже есть `Dockerfile`, что упрощает развёртывание на сервере и демонстрацию проекта.
