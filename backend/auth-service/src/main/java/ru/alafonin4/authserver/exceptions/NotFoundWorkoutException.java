package ru.alafonin4.authserver.exceptions;

public class NotFoundWorkoutException extends RuntimeException{
    public NotFoundWorkoutException(String message){
        super(message);
    }
}

