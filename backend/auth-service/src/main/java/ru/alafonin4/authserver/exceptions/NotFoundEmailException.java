package ru.alafonin4.authserver.exceptions;

public class NotFoundEmailException extends RuntimeException{
    public NotFoundEmailException(String message){
        super(message);
    }
}
