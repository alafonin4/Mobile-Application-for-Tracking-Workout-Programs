package ru.alafonin4.authserver.exceptions;

public class NoRequiredRoleException extends RuntimeException {
    public NoRequiredRoleException(String message) {
        super(message);
    }
}
