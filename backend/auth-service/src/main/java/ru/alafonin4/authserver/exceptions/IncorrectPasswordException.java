package ru.alafonin4.authserver.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    /**
     * Creates a new IncorrectPasswordException instance.
     * @param message human-readable message
     */
    public IncorrectPasswordException(String message){
        super(message);
    }
}
