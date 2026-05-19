# Frontend

`my-app` - это мобильный клиент проекта, построенный на Expo, React Native и Expo Router. Он работает как интерфейс для всех пользовательских сценариев: авторизация, профиль, друзья, тренировки, упражнения, аналитика, соревнования и уведомления.

## Назначение клиента

Frontend отвечает за:

- авторизацию и хранение пользовательской сессии
- отображение и редактирование профиля
- загрузку аватара
- навигацию по вкладкам и внутренним stack-экранам
- отображение друзей, заявок и других профилей
- создание тренировок и программ
- выбор упражнений с фильтрами и избранным
- визуализацию прогресса через графики
- отображение соревнований и лидербордов
- показ уведомлений и персонализированных блоков

## Технологии

- React Native
- Expo 55
- Expo Router
- React 19
- Axios
- Formik
- Yup
- Expo Secure Store
- Expo Image Picker

## Структура каталога

Основные каталоги внутри [my-app](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app):

```text
my-app/
|-- api/          # HTTP-обёртки по доменам
|-- app/          # file-based routing на Expo Router
|-- assets/       # статические ресурсы
|-- components/   # переиспользуемые UI-компоненты
|-- constants/    # константы и конфиг
|-- context/      # контекст сессии и общее состояние
|-- hooks/        # пользовательские хуки
|-- styles/       # стили и темы
|-- utils/        # вспомогательная логика
`-- validation/   # схемы валидации
```

## Организация маршрутов

Клиент использует file-based routing через Expo Router.

### Корневые группы экранов

- `app/(auth)` - экраны входа и регистрации
- `app/(tabs)` - основная часть приложения после авторизации

### Нижняя навигация

В [app/(tabs)/_layout.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/_layout.jsx) описаны основные вкладки:

- соревнования
- друзья
- тренировки
- упражнения
- уведомления
- профиль

### Вложенные разделы

- `app/(tabs)/(workout)` - тренировки, программы, прогресс и экраны деталей
- `app/(tabs)/(exercises)` - каталог упражнений и экран упражнения
- `app/(tabs)/(friends)` - друзья и заявки
- `app/(tabs)/(profile)` - профиль, редактирование, смена пароля, достижения и профиль другого пользователя

## Ключевые пользовательские сценарии

### Аутентификация

Файлы:

- [app/(auth)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(auth)/index.jsx)
- [app/(auth)/sign_up.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(auth)/sign_up.jsx)

Возможности:

- вход
- регистрация
- сохранение токена и пользовательской сессии
- переход между auth и tab-частью приложения

### Профиль

Файлы:

- [app/(tabs)/(profile)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/index.jsx)
- [app/(tabs)/(profile)/edit.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/edit.jsx)
- [app/(tabs)/(profile)/password.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/password.jsx)
- [app/(tabs)/(profile)/[id].jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/[id].jsx)

Возможности:

- просмотр своего профиля
- просмотр профиля другого пользователя
- редактирование личных данных
- выбор аватара из галереи
- смена пароля
- удаление аккаунта и выход
- просмотр spider diagram для другого пользователя

### Друзья и социальные связи

Файл:

- [app/(tabs)/(friends)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(friends)/index.jsx)

Возможности:

- список друзей
- входящие и исходящие заявки
- принятие заявки
- переход в профиль пользователя
- отображение кнопки действия в зависимости от отношений

### Тренировки и программы

Файлы:

- [app/(tabs)/(workout)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/index.jsx)
- [app/(tabs)/(workout)/create-workout.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/create-workout.jsx)
- [app/(tabs)/(workout)/create-program.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/create-program.jsx)
- [app/(tabs)/(workout)/edit-program.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/edit-program.jsx)
- [app/(tabs)/(workout)/workout-details.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/workout-details.jsx)
- [app/(tabs)/(workout)/program-details.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/program-details.jsx)

Возможности:

- просмотр списка тренировок и программ
- создание тренировки
- создание программы
- редактирование программы
- переход к экрану деталей тренировки
- переход к экрану деталей программы
- перенос упражнений из дня тренировочной программы в тренировку

### Каталог упражнений

Файлы:

- [app/(tabs)/(exercises)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(exercises)/index.jsx)
- [app/(tabs)/(exercises)/[id].jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(exercises)/[id].jsx)
- [components/AddExerciseModal.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/components/AddExerciseModal.jsx)

Возможности:

- просмотр списка упражнений
- экран конкретного упражнения
- фильтрация по мышечной группе
- фильтрация по необходимости дополнительного веса
- управление избранными упражнениями
- при выборе в тренировку избранные выводятся первыми

### Прогресс и графики

Файлы:

- [app/(tabs)/(workout)/progress.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/progress.jsx)
- [components/charts/SpiderChart.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/components/charts/SpiderChart.jsx)
- [components/charts/LineChart.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/components/charts/LineChart.jsx)

Возможности:

- spider diagram по мышечным группам
- линейный график по общему прогрессу
- линейный график по мышечной группе
- график по конкретному упражнению
- выбор периода `1`, `3`, `6` месяцев

### Соревнования

Файл:

- [app/(tabs)/CompetitionScreen.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/CompetitionScreen.jsx)

Возможности:

- общий лидерборд
- лидерборд среди друзей
- персональные соревнования
- создание соревнования
- принятие приглашений
- переход из списка участников в профиль пользователя

### Уведомления

Файл:

- [app/(tabs)/notifications.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/notifications.jsx)

Возможности:

- просмотр социальных и персонализированных уведомлений

## API-слой

Все HTTP-запросы сгруппированы по каталогам внутри [api](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/api):

- `api/auth`
- `api/user`
- `api/friends`
- `api/competition`
- `api/exercises`
- `api/workout`

Такой слой нужен, чтобы:

- не писать `axios`-вызовы прямо в экранах
- переиспользовать запросы между экранами
- централизованно менять маршруты и формат данных

## Важная настройка API_URL

Frontend не определяет backend-адрес автоматически. Он берётся из [constants/api_url.js](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/constants/api_url.js).

Сейчас там хранится конкретный адрес:

```js
export const API_URL = "http://10.110.84.28:8083";
```

Перед запуском проверь, что этот адрес соответствует твоему backend.

Частые варианты:

- `http://localhost:8083`
- `http://10.0.2.2:8083` для Android-эмулятора
- локальный IP компьютера в Wi-Fi сети для физического телефона

## Установка и запуск

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

Текущие стартовые скрипты уже учитывают локальные ограничения Expo и запускают приложение без doctor-проверки, которая может падать в offline-среде.

## Полезные команды разработки

Типизация:

```bash
npx tsc --noEmit
```

Тесты:

```bash
npm test
```

Lint:

```bash
npm run lint
```

## Что важно учитывать при разработке

- Приложение использует file-based routing, поэтому новые экраны лучше добавлять в `app/`, а не настраивать вручную в одном месте.
- Некоторые backend-ответы собираются из нескольких сервисов, поэтому экран может зависеть сразу от `user-service`, `social-service` и `workout-service`.
- Для мобильной отладки часто проблема не в коде экрана, а в неправильном `API_URL`.
- Часть экранов использует данные текущего пользователя через сессионный контекст и хук `useUserId`, поэтому любые изменения сессии лучше проверять end-to-end.

## Что стоит проверить вручную

После запуска приложения полезно пройти такие сценарии:

1. Регистрация и логин
2. Загрузка профиля и редактирование профиля
3. Смена пароля
4. Загрузка аватара
5. Создание тренировки
6. Создание программы и перенос упражнений из дня программы в тренировку
7. Добавление упражнения через фильтры и избранное
8. Просмотр графиков прогресса
9. Работа с друзьями и переходами в чужой профиль
10. Просмотр соревнований и приглашений

## Связанные документы

- [README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/README.md) - общий обзор проекта
- [backend/README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/README.md) - описание backend-части
- [package.json](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/package.json) - зависимости и команды клиента
