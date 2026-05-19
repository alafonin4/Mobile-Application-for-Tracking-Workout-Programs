package ru.alafonin4.authserver.pojo;

public class SimpleMessageResponse {
    private String message;

    /**
     * Creates a new SimpleMessageResponse instance.
     */
    public SimpleMessageResponse() {
    }

    /**
     * Creates a new SimpleMessageResponse instance.
     * @param message human-readable message
     */
    public SimpleMessageResponse(String message) {
        this.message = message;
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
}
