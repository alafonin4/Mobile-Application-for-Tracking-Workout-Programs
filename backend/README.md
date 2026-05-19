# Backend

The backend is a set of Spring Boot microservices connected through Eureka and an API Gateway. It is responsible for authentication, user profiles, social features, workouts, training programs, analytics, personalization, and notifications.

## Backend Modules

The [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend) directory contains:

- `discovery-server` - service registration and discovery
- `api-gateway` - single entry point for the mobile client
- `auth-service` - registration, login, JWT, password change, account deletion
- `user-service` - user profile, avatar, basic public user data
- `social-service` - friends, friend requests, competitions, social personalization, notifications
- `workout-service` - workouts, training programs, exercises, favorites, progress analytics, and adaptation

## Backend Architecture

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

Mobile client -> api-gateway :8083 -> business services
```

Each business service uses its own PostgreSQL database. External requests from the client should go through `api-gateway`, while internal service-to-service communication relies on service discovery.

## Service Responsibilities

### `discovery-server`

- registers services in the system
- resolves services by name
- infrastructure dependency for `api-gateway` and internal calls

### `api-gateway`

- proxies client HTTP requests
- holds centralized route definitions
- exposes a single entry point for the mobile application

Route definitions are stored in [api-gateway/src/main/resources/application.properties](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/src/main/resources/application.properties).

### `auth-service`

- user registration
- user login
- JWT generation and validation
- `user-info` lookup by token
- password change
- account deletion

Main controllers:

- [AuthController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/auth-service/src/main/java/ru/alafonin4/authserver/controllers/AuthController.java)
- [TokenController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/auth-service/src/main/java/ru/alafonin4/authserver/controllers/TokenController.java)

### `user-service`

- create the profile after registration
- get a profile by `id`
- update a profile
- delete a profile
- return all users
- store `avatarUrl` and public profile fields

Main controller:

- [UserController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/user-service/src/main/java/ru/alafonin4/userservice/controller/UserController.java)

### `social-service`

- send friend requests
- accept, cancel, reject, and remove friendships
- resolve the relationship between two users
- global competitions
- user-created competitions
- competition invitations
- notification feeds and social insights

Main controllers:

- [FriendRequestController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/src/main/java/ru/alafonin4/socialservice/controllers/FriendRequestController.java)
- [CompetitionController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/src/main/java/ru/alafonin4/socialservice/controllers/CompetitionController.java)
- [SocialInsightsController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/src/main/java/ru/alafonin4/socialservice/controllers/SocialInsightsController.java)

### `workout-service`

- workout CRUD
- training program CRUD
- exercise catalog
- favorite exercises per user
- user progress
- exercise-specific progress
- progress by date range
- personalization, achievements, and program adaptation

Main controllers:

- [WorkoutController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/WorkoutController.java)
- [TrainingProgramController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/TrainingProgramController.java)
- [ExerciseController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/ExerciseController.java)
- [PersonalizationController.java](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/src/main/java/ru/alafonin4/workoutservice/controller/PersonalizationController.java)

## Databases

The backend expects four PostgreSQL databases:

- `auth`
- `users`
- `social`
- `workout`

In the Docker setup, each database already has its own container. For a local non-Docker setup, they must be created manually.

## Configuration

### Docker environment variables

Example environment variables are provided in [backend/.env.example](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/.env.example).

Main parameters:

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

### Local `application.properties`

If the services are started without Docker, datasource and Eureka settings can be overridden through standard Spring Boot properties or by updating `application.properties` inside each module.

## Local Run

From [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
mvn spring-boot:run -pl discovery-server
mvn spring-boot:run -pl api-gateway
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl social-service
mvn spring-boot:run -pl workout-service
```

Recommended order:

1. `discovery-server`
2. `api-gateway`
3. `auth-service`
4. `user-service`
5. `social-service`
6. `workout-service`

## Run with Docker Compose

From [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
docker compose up --build -d
```

Compose file:

- [docker-compose.yml](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/docker-compose.yml)

The Compose stack starts:

- all microservices
- all PostgreSQL databases
- shared network wiring between containers

## Main Endpoint Groups

The following route groups are exposed through `api-gateway`:

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

If you need exact paths and HTTP methods, the best places to inspect are:

- [api-gateway application.properties](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/src/main/resources/application.properties)
- the controller classes in each service

## Build and Test

From [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
mvn test
```

You can also run a single module:

```bash
mvn test -pl workout-service
```

Or:

```bash
mvn spring-boot:run -pl auth-service
```

## Manual Smoke Checks

After the backend starts, it is useful to verify:

1. User registration and login
2. `user-info` lookup by token
3. Profile creation and update
4. Workout creation and workout fetch by `userId`
5. Training program creation and update
6. Exercise catalog and favorites
7. Friend request send and accept flow
8. Global and custom competition endpoints
9. Progress and personalization endpoints

## Notes

- Because the system is split into multiple services, debugging usually requires checking not only the failing module but also `Eureka`, `Gateway`, and the dependent databases.
- Several frontend flows depend on explicitly declared gateway routes, so when a new endpoint is added it usually also needs a route in `api-gateway`.
- Every backend module already includes a `Dockerfile`, which makes deployment and demo setup easier.
