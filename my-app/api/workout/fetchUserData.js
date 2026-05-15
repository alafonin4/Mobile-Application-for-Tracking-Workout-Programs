import { apiGet } from "../client";

export const fetchUserWorkouts = async (userId) => {
  return apiGet(`/api/workouts/user/${userId}`);
};

export const fetchUserPrograms = async (userId) => {
  return apiGet(`/api/training-programs/user/${userId}`);
};
