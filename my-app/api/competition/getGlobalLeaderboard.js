import { apiGet } from "../client";

export const getGlobalLeaderboard = async (userId, months) => {
  return apiGet(`/api/competitions/leaderboards/global/${userId}`, {
    params: { months },
  });
};
