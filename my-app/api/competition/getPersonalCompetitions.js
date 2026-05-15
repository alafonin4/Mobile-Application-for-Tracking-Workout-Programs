import { apiGet } from "../client";

export const getPersonalCompetitions = async (userId) => {
  return apiGet(`/api/competitions/user/${userId}`);
};
