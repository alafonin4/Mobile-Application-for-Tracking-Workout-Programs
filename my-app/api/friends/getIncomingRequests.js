import { apiGet } from "../client";

export const getIncomingRequests = async (userId) => {
  return apiGet(`/api/friendRequests/received/pending/${userId}`);
};
