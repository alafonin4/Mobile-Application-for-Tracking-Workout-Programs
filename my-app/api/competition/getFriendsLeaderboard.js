import { apiGet } from "../client";

export const getFriendsLeaderboard = async (userId, months) => {
  return apiGet(`/api/competitions/leaderboards/friends/${userId}`, {
    params: { months },
  });
};
