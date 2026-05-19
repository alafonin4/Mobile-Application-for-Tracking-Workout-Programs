# Frontend

`my-app` is the mobile client for the project, built with Expo, React Native, and Expo Router. It provides the user-facing flows for authentication, profile management, friends, workouts, exercises, analytics, competitions, and notifications.

## Client Responsibilities

The frontend is responsible for:

- authentication and session persistence
- profile display and editing
- avatar upload
- tab navigation and nested stack navigation
- displaying friends, requests, and other user profiles
- creating workouts and training programs
- selecting exercises with filters and favorites
- visualizing progress through charts
- rendering competitions and leaderboards
- showing notifications and personalized blocks

## Technology Stack

- React Native
- Expo 55
- Expo Router
- React 19
- Axios
- Formik
- Yup
- Expo Secure Store
- Expo Image Picker

## Directory Structure

Main directories inside [my-app](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app):

```text
my-app/
|-- api/          # HTTP wrappers grouped by domain
|-- app/          # file-based routes with Expo Router
|-- assets/       # static assets
|-- components/   # reusable UI components
|-- constants/    # constants and config
|-- context/      # session context and shared state
|-- hooks/        # custom hooks
|-- styles/       # styles and theme pieces
|-- utils/        # helper logic
`-- validation/   # validation schemas
```

## Routing Structure

The client uses file-based routing through Expo Router.

### Top-level route groups

- `app/(auth)` - login and registration screens
- `app/(tabs)` - main application after authentication

### Bottom tab navigation

[app/(tabs)/_layout.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/_layout.jsx) defines the main tabs:

- competitions
- friends
- workouts
- exercises
- notifications
- profile

### Nested sections

- `app/(tabs)/(workout)` - workouts, programs, progress, and detail screens
- `app/(tabs)/(exercises)` - exercise catalog and exercise detail
- `app/(tabs)/(friends)` - friends and requests
- `app/(tabs)/(profile)` - profile, edit profile, change password, achievements, and another user's profile

## Main User Flows

### Authentication

Files:

- [app/(auth)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(auth)/index.jsx)
- [app/(auth)/sign_up.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(auth)/sign_up.jsx)

Features:

- login
- registration
- token and session persistence
- navigation between auth and tabs

### Profile

Files:

- [app/(tabs)/(profile)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/index.jsx)
- [app/(tabs)/(profile)/edit.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/edit.jsx)
- [app/(tabs)/(profile)/password.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/password.jsx)
- [app/(tabs)/(profile)/[id].jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(profile)/[id].jsx)

Features:

- view your own profile
- view another user's profile
- edit personal information
- choose an avatar from the gallery
- change password
- delete account and log out
- show a spider chart for another user

### Friends and relationships

File:

- [app/(tabs)/(friends)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(friends)/index.jsx)

Features:

- friends list
- incoming and outgoing requests
- accept requests
- open a user's profile
- display a relationship-specific action button

### Workouts and programs

Files:

- [app/(tabs)/(workout)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/index.jsx)
- [app/(tabs)/(workout)/create-workout.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/create-workout.jsx)
- [app/(tabs)/(workout)/create-program.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/create-program.jsx)
- [app/(tabs)/(workout)/edit-program.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/edit-program.jsx)
- [app/(tabs)/(workout)/workout-details.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/workout-details.jsx)
- [app/(tabs)/(workout)/program-details.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/program-details.jsx)

Features:

- browse workouts and training programs
- create workouts
- create training programs
- edit programs
- open workout detail
- open program detail
- import exercises from a program day into a workout

### Exercise catalog

Files:

- [app/(tabs)/(exercises)/index.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(exercises)/index.jsx)
- [app/(tabs)/(exercises)/[id].jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(exercises)/[id].jsx)
- [components/AddExerciseModal.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/components/AddExerciseModal.jsx)

Features:

- browse the exercise catalog
- open a specific exercise
- filter by muscle group
- filter by whether additional weight is required
- manage favorite exercises
- show favorites first when selecting an exercise for a workout

### Progress and charts

Files:

- [app/(tabs)/(workout)/progress.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/(workout)/progress.jsx)
- [components/charts/SpiderChart.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/components/charts/SpiderChart.jsx)
- [components/charts/LineChart.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/components/charts/LineChart.jsx)

Features:

- spider chart by muscle group
- line chart for overall progress
- line chart by muscle group
- chart for a specific exercise
- period selection for `1`, `3`, and `6` months

### Competitions

File:

- [app/(tabs)/CompetitionScreen.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/CompetitionScreen.jsx)

Features:

- global leaderboard
- friends leaderboard
- custom competitions
- competition creation
- invitation acceptance
- profile navigation from leaderboard rows

### Notifications

File:

- [app/(tabs)/notifications.jsx](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/app/(tabs)/notifications.jsx)

Features:

- social and personalization notification feed

## API Layer

All HTTP requests are grouped inside [api](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/api):

- `api/auth`
- `api/user`
- `api/friends`
- `api/competition`
- `api/exercises`
- `api/workout`

This layer helps:

- keep `axios` calls out of screen components
- reuse requests across screens
- update routes and payload mapping in one place

## Important `API_URL` Setting

The frontend does not auto-detect the backend address. It uses [constants/api_url.js](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/constants/api_url.js).

Current value:

```js
export const API_URL = "http://10.110.84.28:8083";
```

Before running the app, make sure this address matches your backend.

Common values:

- `http://localhost:8083`
- `http://10.0.2.2:8083` for the Android emulator
- your computer's local IP for a physical phone on the same Wi-Fi network

## Install and Run

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

The current start scripts already account for local Expo limitations and disable the doctor check that may fail in offline environments.

## Useful Development Commands

Type checking:

```bash
npx tsc --noEmit
```

Tests:

```bash
npm test
```

Lint:

```bash
npm run lint
```

## Development Notes

- The app uses file-based routing, so new screens should generally be added in `app/` instead of being wired manually in a single navigation file.
- Some screens combine data from multiple backend services, so a single UI issue may depend on `user-service`, `social-service`, and `workout-service` at the same time.
- For mobile debugging, problems are often caused by an incorrect `API_URL` rather than the screen logic itself.
- Several screens depend on session context and the `useUserId` hook, so session-related changes should be tested end-to-end.

## Manual Checks

After starting the app, it is useful to verify:

1. Registration and login
2. Profile loading and profile editing
3. Password change
4. Avatar upload
5. Workout creation
6. Training program creation and importing program-day exercises into a workout
7. Exercise selection with filters and favorites
8. Progress chart rendering
9. Friend flows and navigation to another user's profile
10. Competitions and invitations

## Related Documents

- [README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/README.md) - overall project overview
- [backend/README.md](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/backend/README.md) - backend documentation
- [package.json](E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/package.json) - client dependencies and scripts
