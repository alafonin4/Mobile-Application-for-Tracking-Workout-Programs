# Mobile Application for Tracking Workout Programs

Мобильное приложение и backend-платформа для ведения тренировок, работы с тренировочными программами, анализа прогресса, социальной активности и соревнований между пользователями.

## О проекте

Репозиторий состоит из двух крупных частей:

- `backend/` - набор Spring Boot микросервисов
- `my-app/` - мобильный клиент на Expo / React Native

Проект реализует полный пользовательский сценарий:

- регистрация и авторизация
- создание и редактирование профиля
- добавление аватара
- работа с друзьями и заявками
- ведение тренировок и тренировочных программ
- каталог упражнений, избранные упражнения и фильтрация
- аналитика прогресса по тренировкам, мышечным группам и упражнениям
- общие и пользовательские соревнования
- персонализация, достижения и уведомления

## Архитектура

Backend построен как микросервисная система с Eureka Service Discovery и единым API Gateway.

### Сервисы

- `discovery-server` - реестр сервисов, порт `8761`
- `api-gateway` - единая точка входа для клиента, порт `8083`
- `auth-service` - авторизация, JWT, смена пароля, удаление аккаунта, порт `8082`
- `user-service` - профиль пользователя, аватар, публичные данные, порт `8084`
- `social-service` - друзья, заявки, соревнования, социальная персонализация, уведомления, порт `8085`
- `workout-service` - тренировки, программы, упражнения, избранное, аналитика прогресса, персонализация, порт `8086`

### Взаимодействие компонентов

```text
Мобильный клиент
        |
        v
  API Gateway :8083
        |
        +--> auth-service
        +--> user-service
        +--> social-service
        +--> workout-service
                 ^
                 |
          PostgreSQL БД по сервисам
```

Клиент должен отправлять внешние запросы через `api-gateway`. Внутреннее взаимодействие сервисов идёт через Eureka и `lb://...` маршруты.

## Основные возможности

### Аутентификация и аккаунт

- регистрация и вход по email/паролю
- хранение JWT-сессии на клиенте
- изменение пароля
- удаление аккаунта

### Профиль пользователя

- просмотр своего профиля и профиля другого пользователя
- редактирование имени, фамилии, bio, веса и цели
- загрузка аватара из галереи
- отображение достижений, рекордов и персонализированных инсайтов

### Тренировки и программы

- создание тренировок с упражнениями, подходами, весом и повторениями
- создание, просмотр и редактирование тренировочных программ
- импорт упражнений в тренировку из выбранного дня программы
- отдельные экраны деталей тренировки и программы

### Каталог упражнений

- список всех упражнений
- фильтрация по группе мышц
- фильтрация по необходимости дополнительного веса
- избранные упражнения пользователя
- при выборе упражнения в тренировку избранные показываются первыми

### Аналитика прогресса

- общий прогресс пользователя
- прогресс по мышечным группам
- прогресс по конкретному упражнению
- spider diagram по мышечным группам
- графики динамики за `1`, `3` и `6` месяцев
- серверный составной расчёт прогресса на основе объёма, интенсивности, плотности, регулярности, восстановления, баланса и рекордов

### Социальные функции

- отправка, принятие, отмена и отклонение заявок в друзья
- переход из списка друзей в профиль пользователя
- просмотр социального статуса по отношению к другому пользователю

### Соревнования

- общий ежемесячный рейтинг по прогрессу среди всех пользователей
- общий ежемесячный рейтинг среди друзей
- пользовательские соревнования с приглашением друзей
- поддержка разных целей соревнования:
  - прогресс
  - количество повторений в упражнении
  - количество тренировок

### Персонализация и уведомления

- персональные достижения
- адаптация тренировочных программ
- социальная персонализация
- внутриигровые уведомления по друзьям, соревнованиям и достижениям

## Структура репозитория

```text
.
|-- backend/
|   |-- api-gateway/
|   |-- auth-service/
|   |-- discovery-server/
|   |-- social-service/
|   |-- user-service/
|   `-- workout-service/
`-- my-app/
    |-- api/
    |-- app/
    |-- components/
    |-- constants/
    |-- context/
    |-- hooks/
    `-- utils/
```

## Технологический стек

### Backend

- Java 21
- Spring Boot 3.2
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Docker / Docker Compose

### Frontend

- React Native
- Expo 55
- Expo Router
- React 19
- Axios
- Formik
- Yup
- Expo Secure Store
- Expo Image Picker

## Требования для локального запуска

- JDK 21
- Maven 3.9+
- Node.js 20+
- npm
- PostgreSQL 16+ или Docker
- Android Studio / эмулятор / Expo Go

## Быстрый старт

### Вариант 1. Запуск backend через Docker

1. Перейти в каталог [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend)
2. Скопировать `backend/.env.example` в `.env`
3. При необходимости поменять пароли и параметры БД
4. Запустить:

```bash
cd backend
docker compose up --build -d
```

Compose поднимет:

- `discovery-server`
- `api-gateway`
- `auth-service`
- `user-service`
- `social-service`
- `workout-service`
- отдельный PostgreSQL-контейнер для каждого бизнес-сервиса

### Вариант 2. Локальный запуск backend без Docker

Нужно создать PostgreSQL-базы:

- `auth`
- `users`
- `social`
- `workout`

Затем запустить сервисы из [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend) в таком порядке:

```bash
mvn spring-boot:run -pl discovery-server
mvn spring-boot:run -pl api-gateway
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl social-service
mvn spring-boot:run -pl workout-service
```

Рекомендуемый порядок запуска:

1. `discovery-server`
2. `api-gateway`
3. `auth-service`
4. `user-service`
5. `social-service`
6. `workout-service`

## Запуск мобильного клиента

Из каталога [my-app](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app):

```bash
npm install
npm run start
```

Дополнительные команды:

```bash
npm run android
npm run ios
npm run web
```

## Важная настройка frontend API

Адрес gateway сейчас задаётся вручную в [my-app/constants/api_url.js](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/constants/api_url.js):

```js
export const API_URL = "http://10.110.84.28:8083";
```

Перед локальным запуском нужно убедиться, что это значение указывает на реальный адрес `api-gateway` в вашей сети.

Примеры:

- эмулятор Android Studio: часто подходит `http://10.0.2.2:8083`
- физическое устройство в одной Wi-Fi сети: IP вашего компьютера, например `http://192.168.1.10:8083`
- web / локальная отладка на том же ПК: может подойти `http://localhost:8083`

## Основные backend-маршруты

Ниже перечислены ключевые публичные группы endpoint'ов, доступные через `api-gateway`:

- `/api/auth/*` - регистрация, вход, смена пароля, удаление аккаунта
- `/token/*` - валидация токена и получение информации о пользователе
- `/api/users/*` - работа с профилем пользователя
- `/api/friendRequests/*` - заявки в друзья и отношения между пользователями
- `/api/competitions/*` - рейтинги и пользовательские соревнования
- `/api/workouts/*` - тренировки и прогресс
- `/api/training-programs/*` - тренировочные программы
- `/api/exercises/*` - каталог упражнений и избранное
- `/api/personalization/*` - персонализация и адаптация программ
- `/api/social-personalization/*` - социальная персонализация
- `/api/notifications/*` - уведомления

Подробные route-правила gateway находятся в [backend/api-gateway/src/main/resources/application.properties](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/src/main/resources/application.properties).

## Проверка и тесты

### Backend

```bash
cd backend
mvn test
```

### Frontend

Типизация:

```bash
cd my-app
npx tsc --noEmit
```

Jest:

```bash
npm test
```

Lint:

```bash
npm run lint
```

## Полезные файлы

- [backend/README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/README.md) - подробнее о backend
- [my-app/README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/README.md) - подробнее о мобильном клиенте
- [backend/docker-compose.yml](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/docker-compose.yml) - запуск backend в контейнерах
- [backend/.env.example](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/.env.example) - пример переменных окружения
- [backend/pom.xml](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/pom.xml) - Maven aggregator для всех сервисов
- [my-app/package.json](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/package.json) - frontend-зависимости и команды

## Текущее состояние проекта

Проект активно развивается, поэтому в репозитории уже реализованы и базовые CRUD-сценарии, и более сложные модули:

- многошаговый подсчёт прогресса
- дружба и социальные отношения
- ежемесячные и пользовательские соревнования
- персонализация
- избранные упражнения
- графическая аналитика на клиенте

При этом часть конфигурации всё ещё ориентирована на локальную разработку, поэтому перед первым запуском стоит проверить:

- `API_URL` во frontend
- параметры PostgreSQL
- доступность `Eureka`
- порты `8082`, `8083`, `8084`, `8085`, `8086`, `8761`

## Лицензия

В репозитории сейчас не добавлен отдельный файл лицензии. При необходимости его стоит определить отдельно.
