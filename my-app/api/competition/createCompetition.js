import { apiPost } from "../client";

export const createCompetition = async (payload) => {
  return apiPost("/api/competitions", payload);
};
