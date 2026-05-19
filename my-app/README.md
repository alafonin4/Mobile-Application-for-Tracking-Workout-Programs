# Frontend

Mobile client for the workout tracking platform built with Expo, React Native, and Expo Router.

## Responsibilities

- Authentication flow
- Profile viewing and editing
- Friends and social interactions
- Competitions and leaderboards
- Workout logging
- Training program management
- Progress analytics and charts
- Personalization, achievements, and personal records
- In-app notifications

## Stack

- Expo 55
- React Native 0.83
- React 19
- Expo Router
- Axios
- Formik
- Yup

## Project Layout

```text
my-app/
├── api/          # HTTP requests grouped by domain
├── app/          # Expo Router screens
├── components/   # Reusable UI blocks
├── constants/    # Shared constants and theme data
├── hooks/        # Reusable hooks
├── utils/        # Frontend helpers and mapping logic
└── assets/       # Static media
```

## Important Feature Areas

### Authentication

- registration
- login
- secure token storage
- account management actions

### Profile

- current user profile
- other user profile preview
- avatar editing
- fitness goal display and editing
- achievements preview and full achievements screen
- personal records and wellness insights

### Workouts and programs

- workout CRUD
- training program CRUD
- exercise catalog and favorites
- program adaptation recommendations

### Progress

- summary metrics
- muscle-group spider chart
- weekly activity bar chart
- exercise-specific progress

### Social

- friend requests
- accepted friends
- user-created competitions
- leaderboards

### Notifications

The app currently includes an in-app notifications screen for:

- friend requests
- accepted requests
- competition invites
- smart reminders
- achievement unlocks

## Environment Expectations

The app expects the backend services to be available through the API Gateway. If your local backend host or port changes, update the frontend API configuration files in [my-app/api](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/api).

## Scripts

From [package.json](/E:/Diplom/Mobile-Application-for-Tracking-Workout-Programs/my-app/package.json):

```bash
npm install
npm run start
npm run android
npm run ios
npm run web
npm test
npm run lint
```

## Development Notes

- Routing is file-based through Expo Router
- API calls are normalized so screens can show clear backend error messages
- Personalization data is assembled from multiple backend endpoints
- The UI supports both self-profile and other-user profile scenarios

## Recommended Manual Checks

1. Sign in and verify token persistence
2. Open profile, achievements, and notifications
3. Create and edit a workout
4. Open the progress screen and verify charts render with zero values when data is missing
5. Open a training program and verify adaptation suggestions
6. Test friend request and competition invite flows
