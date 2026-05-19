package ru.alafonin4.authserver.exceptions;

import java.time.Instant;

public class ApiErrorResponse {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    /**
     * Creates a new ApiErrorResponse instance.
     * @param status status
     * @param error error
     * @param message human-readable message
     * @param path path
     */
    public ApiErrorResponse(int status, String error, String message, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Returns the timestamp.
     * @return result of the operation
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the status.
     * @return calculated numeric value
     */
    public int getStatus() {
        return status;
    }

    /**
     * Returns the error.
     * @return resulting text value
     */
    public String getError() {
        return error;
    }

    /**
     * Returns the message.
     * @return resulting text value
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the path.
     * @return resulting text value
     */
    public String getPath() {
        return path;
    }
}
