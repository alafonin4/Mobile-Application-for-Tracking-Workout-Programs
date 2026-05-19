package ru.alafonin4.authserver.exceptions;

public class NotFoundEmailException extends RuntimeException{
    /**
     * Creates a new NotFoundEmailException instance.
     * @param message human-readable message
     */
    public NotFoundEmailException(String message){
        super(message);
    }
}
