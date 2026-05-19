package ru.alafonin4.socialservice.dto;

import lombok.Builder;
import lombok.Data;


public class FriendRequestDTO {
    private Long senderId;
    private Long receiverId;

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
}
