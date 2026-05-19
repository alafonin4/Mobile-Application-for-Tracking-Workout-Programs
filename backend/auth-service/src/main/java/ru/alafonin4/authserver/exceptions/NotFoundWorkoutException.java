package ru.alafonin4.authserver.exceptions;

public class NotFoundWorkoutException extends RuntimeException{
    /**
     * Creates a new NotFoundWorkoutException instance.
     * @param message human-readable message
     */
    public NotFoundWorkoutException(String message){
        super(message);
    }
}

