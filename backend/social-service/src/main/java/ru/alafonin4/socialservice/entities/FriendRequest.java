package ru.alafonin4.socialservice.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_requests")
public class FriendRequest {

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long senderId;
    private Long receiverId;
    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;

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
     * Returns the created at.
     * @return result of the operation
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Updates the created at.
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the responded at.
     * @return result of the operation
     */
    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    /**
     * Updates the responded at.
     * @param respondedAt new responded at
     */
    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }
}
