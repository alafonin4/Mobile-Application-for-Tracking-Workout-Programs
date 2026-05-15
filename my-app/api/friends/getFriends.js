import { apiGet } from "../client";

export const getFriends = async (userId) => {
  return apiGet(`/api/friendRequests/approved/${userId}`);
};
