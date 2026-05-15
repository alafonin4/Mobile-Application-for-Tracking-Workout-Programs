import { apiGet } from "../client";

export const fetchSocialPersonalization = async (userId) => {
  return apiGet(`/api/social-personalization/user/${userId}`);
};
