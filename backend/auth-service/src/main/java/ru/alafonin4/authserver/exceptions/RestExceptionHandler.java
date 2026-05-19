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

    /**
     * Handles no such role exists exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @ExceptionHandler(NoSuchRoleExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleNoSuchRoleExistsException(
            NoSuchRoleExistsException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    /**
     * Handles user already exist exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistException(
            UserAlreadyExistException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    /**
     * Handles method argument not valid exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
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

    /**
     * Handles not found email exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @ExceptionHandler(NotFoundEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundEmailException(
            NotFoundEmailException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    /**
     * Handles incorrect password exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
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

    /**
     * Handles invalid token exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTokenException(
            InvalidTokenException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
    }

    /**
     * Handles no required role exception and prepares the response payload.
     * @param exception exception
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @ExceptionHandler(NoRequiredRoleException.class)
    public ResponseEntity<ApiErrorResponse> handleNoRequiredRoleException(
            NoRequiredRoleException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.FORBIDDEN, exception.getMessage(), request);
    }

    /**
     * Handles unexpected exception and prepares the response payload.
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", request);
    }

    /**
     * Builds the error.
     * @param status status
     * @param message human-readable message
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
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
