package ru.alafonin4.authserver.exceptions;

public class NoSuchRoleExistsException extends RuntimeException{
    public NoSuchRoleExistsException(String message){
        super(message);
    }
}
