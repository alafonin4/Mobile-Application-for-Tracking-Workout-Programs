package ru.alafonin4.socialservice.dto;

import ru.alafonin4.socialservice.enums.FriendRequestStatus;

public class FriendRelationshipResponse {
    private Long requestId;
    private Long senderId;
    private Long receiverId;
    private FriendRequestStatus status;
    private String relationType;

    /**
     * Returns the identifier of the request.
     * @return result of the operation
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * Updates the identifier of the request.
     * @param requestId identifier of the request
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * Returns the identifier of the sender.
     * @return result of the operation
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     * Updates the identifier of the sender.
     * @param senderId identifier of the sending user
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * Returns the identifier of the receiver.
     * @return result of the operation
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * Updates the identifier of the receiver.
     * @param receiverId identifier of the receiving user
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * Returns the status.
     * @return result of the operation
     */
    public FriendRequestStatus getStatus() {
        return status;
    }

    /**
     * Updates the status.
     * @param status new status
     */
    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

    /**
     * Returns the relation type.
     * @return resulting text value
     */
    public String getRelationType() {
        return relationType;
    }

    /**
     * Updates the relation type.
     * @param relationType new relation type
     */
    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}
