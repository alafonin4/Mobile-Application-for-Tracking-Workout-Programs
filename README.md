# Mobile Application for Tracking Workout Programs

Mobile application and backend platform for workout logging, training program management, progress analytics, social features, and competitions between users.

## Overview

This repository contains two major parts:

- `backend/` - Spring Boot microservices
- `my-app/` - Expo / React Native mobile client

The project covers the full user flow:

- registration and login
- profile creation and editing
- avatar upload
- friend requests and friendships
- workout and training program management
- exercise catalog, favorites, and filtering
- progress analytics by workout, muscle group, and exercise
- global and custom competitions
- personalization, achievements, and notifications

## Architecture

The backend is built as a microservice system with Eureka Service Discovery and a single API Gateway.

### Services

- `discovery-server` - service registry, port `8761`
- `api-gateway` - public entry point for the client, port `8083`
- `auth-service` - authentication, JWT, password change, account deletion, port `8082`
- `user-service` - user profile, avatar, public user data, port `8084`
- `social-service` - friends, requests, competitions, social personalization, notifications, port `8085`
- `workout-service` - workouts, training programs, exercises, favorites, progress analytics, personalization, port `8086`

### Component interaction

```text
Mobile client
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
      PostgreSQL databases per service
```

The client should send external requests through `api-gateway`. Internal service-to-service communication uses Eureka and `lb://...` routes.

## Main Features

### Authentication and account

- registration and login with email and password
- JWT session persistence on the client
- password change
- account deletion

### User profile

- view your own profile and other users' profiles
- edit first name, last name, bio, weight, and goal
- upload an avatar from the gallery
- show achievements, records, and personalized insights

### Workouts and programs

- create workouts with exercises, sets, weight, and repetitions
- create, view, and edit training programs
- import exercises into a workout from a selected training day
- dedicated workout and program detail screens

### Exercise catalog

- browse all exercises
- filter by muscle group
- filter by whether additional weight is required
- manage per-user favorite exercises
- show favorites first in the workout exercise picker

### Progress analytics

- overall user progress
- progress by muscle group
- progress for a specific exercise
- spider diagram for muscle groups
- charts for `1`, `3`, and `6` month periods
- server-side composite progress calculation based on volume, intensity, density, consistency, recovery, balance, and records

### Social features

- send, accept, cancel, reject, and remove friendships
- open a user's profile from the friends screen
- show relationship-specific actions on another user's profile

### Competitions

- global monthly progress leaderboard for all users
- monthly leaderboard among friends
- custom competitions with invited friends
- support for multiple competition goals:
  - progress
  - exercise repetitions
  - workout count

### Personalization and notifications

- personal achievements
- training program adaptation
- social personalization
- in-app notifications for friends, competitions, and achievements

## Repository Structure

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

## Tech Stack

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

## Requirements

- JDK 21
- Maven 3.9+
- Node.js 20+
- npm
- PostgreSQL 16+ or Docker
- Android Studio, emulator, or Expo Go

## Quick Start

### Option 1. Run the backend with Docker

1. Go to [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend)
2. Copy `backend/.env.example` to `.env`
3. Update database passwords and settings if needed
4. Run:

```bash
cd backend
docker compose up --build -d
```

The Compose stack starts:

- `discovery-server`
- `api-gateway`
- `auth-service`
- `user-service`
- `social-service`
- `workout-service`
- one PostgreSQL container per business service

### Option 2. Run the backend locally without Docker

Create the following PostgreSQL databases:

- `auth`
- `users`
- `social`
- `workout`

Then start the services from [backend](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend):

```bash
mvn spring-boot:run -pl discovery-server
mvn spring-boot:run -pl api-gateway
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl social-service
mvn spring-boot:run -pl workout-service
```

Recommended startup order:

1. `discovery-server`
2. `api-gateway`
3. `auth-service`
4. `user-service`
5. `social-service`
6. `workout-service`

## Start the Mobile Client

From [my-app](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app):

```bash
npm install
npm run start
```

Additional commands:

```bash
npm run android
npm run ios
npm run web
```

## Important Frontend API Setting

The gateway address is currently configured manually in [my-app/constants/api_url.js](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/constants/api_url.js):

```js
export const API_URL = "http://10.110.84.28:8083";
```

Before running locally, make sure this value points to the actual `api-gateway` address in your environment.

Common examples:

- Android Studio emulator: `http://10.0.2.2:8083`
- physical device on the same Wi-Fi network: your computer's local IP, for example `http://192.168.1.10:8083`
- local web debugging on the same machine: `http://localhost:8083`

## Main Backend Route Groups

The following public endpoint groups are exposed through `api-gateway`:

- `/api/auth/*` - registration, login, password change, account deletion
- `/token/*` - token validation and current user info
- `/api/users/*` - user profile management
- `/api/friendRequests/*` - friend requests and user relationships
- `/api/competitions/*` - leaderboards and custom competitions
- `/api/workouts/*` - workouts and progress
- `/api/training-programs/*` - training programs
- `/api/exercises/*` - exercise catalog and favorites
- `/api/personalization/*` - personalization and program adaptation
- `/api/social-personalization/*` - social personalization
- `/api/notifications/*` - notifications

Detailed gateway route definitions are stored in [backend/api-gateway/src/main/resources/application.properties](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/api-gateway/src/main/resources/application.properties).

## Testing and Verification

### Backend

```bash
cd backend
mvn test
```

### Frontend

Type checking:

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

## Useful Files

- [backend/README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/README.md) - backend details
- [my-app/README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/README.md) - frontend details
- [backend/docker-compose.yml](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/docker-compose.yml) - backend container orchestration
- [backend/.env.example](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/.env.example) - example environment variables
- [backend/pom.xml](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/pom.xml) - Maven aggregator for all services
- [my-app/package.json](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/package.json) - frontend dependencies and scripts

## Current Project State

The project already includes both standard CRUD scenarios and more advanced modules:

- multi-step progress calculation
- friendship and social relationship flows
- monthly and custom competitions
- personalization
- favorite exercises
- chart-based analytics in the mobile client

At the same time, part of the configuration is still optimized for local development, so before the first run it is worth checking:

- frontend `API_URL`
- PostgreSQL configuration
- `Eureka` availability
- ports `8082`, `8083`, `8084`, `8085`, `8086`, and `8761`

## License

There is currently no dedicated license file in the repository. Add one separately if needed.
