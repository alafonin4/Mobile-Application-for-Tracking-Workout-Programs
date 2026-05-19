package ru.alafonin4.authserver.exceptions;

public class NoRequiredRoleException extends RuntimeException {
    /**
     * Creates a new NoRequiredRoleException instance.
     * @param message human-readable message
     */
    public NoRequiredRoleException(String message) {
        super(message);
    }
}
