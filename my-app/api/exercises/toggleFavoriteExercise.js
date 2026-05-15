import { apiDelete, apiPut } from "../client";

export const addFavoriteExercise = async (exerciseId, userId) => {
  await apiPut(`/api/exercises/${exerciseId}/favorite/${userId}`);
};

export const removeFavoriteExercise = async (exerciseId, userId) => {
  await apiDelete(`/api/exercises/${exerciseId}/favorite/${userId}`);
};
