package ru.alafonin4.authserver.exceptions;

public class NoSuchRoleExistsException extends RuntimeException{
    /**
     * Creates a new NoSuchRoleExistsException instance.
     * @param message human-readable message
     */
    public NoSuchRoleExistsException(String message){
        super(message);
    }
}
