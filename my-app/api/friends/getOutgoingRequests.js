import { apiGet } from "../client";

export const getOutgoingRequests = async (userId) => {
  return apiGet(`/api/friendRequests/sent/pending/${userId}`);
};
