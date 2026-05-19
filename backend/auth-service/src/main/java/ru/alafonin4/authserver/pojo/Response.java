package ru.alafonin4.authserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Response {
    private String message;

    /**
     * Creates a new Response instance.
     * @param message human-readable message
     */
    public Response(String message) {
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
