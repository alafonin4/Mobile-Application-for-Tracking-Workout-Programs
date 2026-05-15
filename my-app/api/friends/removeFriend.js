import { apiPut } from "../client";

export const removeFriend = async (requestId, currentUserId) => {
  return apiPut(
    `/api/friendRequests/${requestId}/remove`,
    null,
    {
      params: { currentUserId },
    }
  );
};
