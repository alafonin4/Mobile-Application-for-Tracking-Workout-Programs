# Backend

Backend layer for the workout tracking platform. The backend is organized as a Spring Boot microservice system with shared discovery and a single API Gateway entry point.

## Services

### `discovery-server`

- Service registry based on Eureka
- Runs on port `8761`
- Keeps track of all available microservices

### `api-gateway`

- Public entry point for the mobile client
- Runs on port `8083`
- Routes requests to all business services
- Contains centralized route configuration for authentication, users, social features, workouts, personalization, and notifications

### `auth-service`

- Runs on port `8082`
- Handles registration, login, password change, token validation, and account deletion
- Generates JWT tokens and persists user sessions

### `user-service`

- Runs on port `8084`
- Manages user profile data
- Stores public profile information, avatar URL, fitness goal, and profile editing data

### `social-service`

- Runs on port `8085`
- Handles friend requests, accepted friendships, user-created competitions, invites, leaderboards, social achievements, and notification aggregation

### `workout-service`

- Runs on port `8086`
- Stores workouts, training programs, exercises, favorites, progress analytics, personal records, achievements, and adaptive recommendations

## Module Structure

Each service follows the usual Spring layout:

```text
src/main/java/.../
├── config
├── controller or controllers
├── dto / pojo
├── entity or model
├── exception or exceptions
├── repository or repositories
└── service or services
```

## Key Functional Areas

### Authentication and security

- `auth-service` registers and authenticates users
- `TokenController` validates bearer tokens for other services
- Passwords are encoded before persistence
- Gateway routes auth endpoints to the authentication service

### User management

- `user-service` exposes CRUD endpoints for user profiles
- Profiles include goal-oriented fields used by the personalization layer

### Workout tracking

- Workouts contain exercises and nested sets
- Training programs are organized by training days
- Exercise catalog and favorites support program creation

### Analytics and personalization

- Workout progress summaries
- Exercise-specific progress
- Muscle-group distribution
- Personal records
- Achievements
- Recovery score
- Muscle balance
- Smart reminders
- Training program adaptation suggestions

### Social features

- Friend request flow with pending, accepted, rejected, canceled, and removed states
- User-created competitions with invitations and participation tracking
- Global and friends leaderboards
- Social achievements such as completed competitions and percentile-based ranking

### Notifications

The current implementation provides in-app notifications without push delivery:

- incoming friend requests
- accepted friend requests
- competition invites
- smart reminders
- new achievements

## Databases

The backend expects four PostgreSQL databases:

- `auth`
- `users`
- `social`
- `workout`

Connection settings are stored in each service's [application.properties](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/auth-service/src/main/resources/application.properties) file and can be changed per environment.

## Build And Run

From the [backend](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend) directory:

```bash
mvn test
```

Run a single service:

```bash
mvn spring-boot:run -pl workout-service
```

Recommended startup order:

1. `discovery-server`
2. `api-gateway`
3. `auth-service`
4. `user-service`
5. `social-service`
6. `workout-service`

## Docker Deployment

The backend now includes a `Dockerfile` in each microservice directory:

- [backend/discovery-server/Dockerfile](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/discovery-server/Dockerfile)
- [backend/api-gateway/Dockerfile](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/Dockerfile)
- [backend/auth-service/Dockerfile](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/auth-service/Dockerfile)
- [backend/user-service/Dockerfile](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/user-service/Dockerfile)
- [backend/social-service/Dockerfile](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/social-service/Dockerfile)
- [backend/workout-service/Dockerfile](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/workout-service/Dockerfile)

The full server stack can be started with [backend/docker-compose.yml](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/docker-compose.yml).

### Prepare environment

1. Copy [backend/.env.example](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/.env.example) to `.env`
2. Replace database passwords and optional logging settings
3. Make sure Docker and Docker Compose are available on the server

### Start the stack

```bash
cd backend
docker compose up --build -d
```

### What the Compose stack includes

- `discovery-server`
- `api-gateway`
- `auth-service`
- `user-service`
- `social-service`
- `workout-service`
- one PostgreSQL container per business microservice

### Exposed ports

- `8761` for Eureka dashboard
- `8083` for the public API Gateway
- `8082`, `8084`, `8085`, `8086` for direct service access when needed

### Environment-driven configuration

Application properties now support environment overrides for:

- datasource URL, username, and password
- JPA DDL mode and SQL logging
- Eureka default zone
- service ports
- gateway logging levels

## API Notes

- Public mobile traffic should go through `api-gateway`
- Services use meaningful HTTP status codes for validation errors, missing entities, conflicts, and forbidden actions
- Error responses are normalized so the frontend can display user-facing explanations

## Testing

Primary verification command:

```bash
mvn test
```

Because the project uses multiple services, it is also helpful to smoke-test:

1. registration and login
2. profile fetch/update
3. workout CRUD
4. training program CRUD
5. friend request flow
6. competition invitations and leaderboard endpoints
7. personalization and notification endpoints

## Documentation

JavaDoc has been expanded across backend methods so service contracts, helper behavior, and request/response responsibilities are easier to understand directly from code.
