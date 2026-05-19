package ru.alafonin4.workoutservice.dto;

public class SmartReminderDto {
    private String code;
    private String title;
    private String message;
    private String severity;
    private String createdAt;

    /**
     * Returns the code.
     * @return resulting text value
     */
    public String getCode() {
        return code;
    }

    /**
     * Updates the code.
     * @param code stable machine-readable code
     */
    public void setCode(String code) {
        this.code = code;
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
     * Returns the severity.
     * @return resulting text value
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Updates the severity.
     * @param severity severity level of the reminder
     */
    public void setSeverity(String severity) {
        this.severity = severity;
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
}
