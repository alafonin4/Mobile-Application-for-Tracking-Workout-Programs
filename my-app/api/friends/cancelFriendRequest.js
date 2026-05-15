import { apiPut } from "../client";

export const cancelFriendRequest = async (requestId, currentUserId) => {
  return apiPut(
    `/api/friendRequests/${requestId}/cancel`,
    null,
    {
      params: { currentUserId },
    }
  );
};
