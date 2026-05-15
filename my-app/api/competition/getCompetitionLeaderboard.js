import { apiGet } from "../client";

export const getCompetitionLeaderboard = async (competitionId, userId) => {
  return apiGet(`/api/competitions/${competitionId}/leaderboard`, {
    params: { userId },
  });
};
