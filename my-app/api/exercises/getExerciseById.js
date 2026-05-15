import { apiGet } from "../client";

export const getExerciseById = async (id) => {
  return apiGet(`/api/exercises/${id}`);
};
