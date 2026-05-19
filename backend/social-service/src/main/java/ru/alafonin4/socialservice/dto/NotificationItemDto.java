package ru.alafonin4.socialservice.dto;

public class NotificationItemDto {
    private String id;
    private String type;
    private String title;
    private String message;
    private String createdAt;
    private String priority;
    private Long relatedUserId;
    private Long requestId;
    private Long competitionId;

    /**
     * Returns the id.
     * @return resulting text value
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the id.
     * @param id identifier of the target record
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the type.
     * @return resulting text value
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type.
     * @param type new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the title.
     * @return resulting text value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title.
     * @param title human-readable title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the message.
     * @return resulting text value
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the message.
     * @param message human-readable message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the created at.
     * @return resulting text value
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Updates the created at.
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the priority.
     * @return resulting text value
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Updates the priority.
     * @param priority new priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Returns the identifier of the related user.
     * @return result of the operation
     */
    public Long getRelatedUserId() {
        return relatedUserId;
    }

    /**
     * Updates the identifier of the related user.
     * @param relatedUserId identifier of the related user
     */
    public void setRelatedUserId(Long relatedUserId) {
        this.relatedUserId = relatedUserId;
    }

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
     * Returns the identifier of the competition.
     * @return result of the operation
     */
    public Long getCompetitionId() {
        return competitionId;
    }

    /**
     * Updates the identifier of the competition.
     * @param competitionId identifier of the competition
     */
    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }
}
