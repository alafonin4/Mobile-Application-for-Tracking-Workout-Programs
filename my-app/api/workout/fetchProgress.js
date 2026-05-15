import { apiGet } from "../client";

export const fetchWorkoutProgress = async (userId, months) => {
  return apiGet(`/api/workouts/progress/user/${userId}`, {
    params: { months },
  });
};

export const fetchExerciseProgress = async (userId, exerciseId, months) => {
  return apiGet(
    `/api/workouts/progress/user/${userId}/exercise/${exerciseId}`,
    {
      params: { months },
    }
  );
};
