package ru.alafonin4.authserver.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    /**
     * Creates a new UserAlreadyExistException instance.
     * @param message human-readable message
     */
    public UserAlreadyExistException(String message){
        super(message);
    }
}
