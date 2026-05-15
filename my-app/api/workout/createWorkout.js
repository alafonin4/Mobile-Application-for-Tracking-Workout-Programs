import { apiPost } from "../client";

export const createWorkout = async (workoutData) => {
  return apiPost("/api/workouts", workoutData);
};
