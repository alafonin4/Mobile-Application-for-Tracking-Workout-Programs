package ru.alafonin4.authserver.exceptions;

public class InvalidTokenException extends RuntimeException{
    /**
     * Creates a new InvalidTokenException instance.
     * @param message human-readable message
     */
    public InvalidTokenException(String message){
        super(message);
    }
}
