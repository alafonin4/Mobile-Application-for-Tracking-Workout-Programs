import { apiGet } from "../client";

export const getExerciseCatalog = async (userId, filters = {}) => {
  return apiGet("/api/exercises/catalog", {
    params: {
      userId,
      muscleGroup: filters.muscleGroup,
      requiresAdditionalWeight: filters.requiresAdditionalWeight,
    },
  });
};
