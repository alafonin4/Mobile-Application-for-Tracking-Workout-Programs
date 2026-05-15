package ru.alafonin4.authserver.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NoSuchRoleExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchRoleExistsException(
            NoSuchRoleExistsException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistException(
            UserAlreadyExistException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildError(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(NotFoundEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundEmailException(
            NotFoundEmailException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(
            IncorrectPasswordException exception,
            HttpServletRequest request
    ) {
        String message = exception.getMessage() == null || exception.getMessage().isBlank()
                ? "Incorrect password."
                : exception.getMessage();
        return buildError(HttpStatus.UNAUTHORIZED, message, request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTokenException(
            InvalidTokenException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
    }

    @ExceptionHandler(NoRequiredRoleException.class)
    public ResponseEntity<ApiErrorResponse> handleNoRequiredRoleException(
            NoRequiredRoleException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.FORBIDDEN, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", request);
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(status)
                .body(new ApiErrorResponse(
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }
}
