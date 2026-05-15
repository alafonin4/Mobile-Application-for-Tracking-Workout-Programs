import { apiGet } from "../client";

export const getAllExercises = async () => {
  return apiGet("/api/exercises");
};
