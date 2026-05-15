import { apiPut } from "../client";

export const acceptCompetitionInvite = async (competitionId, userId) => {
  return apiPut(`/api/competitions/${competitionId}/participants/${userId}/accept`);
};
