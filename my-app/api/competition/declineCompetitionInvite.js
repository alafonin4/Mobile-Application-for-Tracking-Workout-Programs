import { apiPut } from "../client";

export const declineCompetitionInvite = async (competitionId, userId) => {
  return apiPut(`/api/competitions/${competitionId}/participants/${userId}/decline`);
};
