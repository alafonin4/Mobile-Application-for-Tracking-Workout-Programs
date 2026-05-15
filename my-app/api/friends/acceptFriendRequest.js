import { apiPut } from "../client";

export const acceptFriendRequest = async (requestId) => {
  return apiPut(`/api/friendRequests/${requestId}/accept`);
};
