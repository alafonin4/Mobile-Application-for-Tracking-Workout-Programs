import { apiGet } from "../client";

export const fetchPersonalizationProfile = async (userId) => {
  return apiGet(`/api/personalization/user/${userId}`);
};

export const fetchProgramAdaptation = async (userId, programId) => {
  return apiGet(`/api/personalization/user/${userId}/program/${programId}`);
};
