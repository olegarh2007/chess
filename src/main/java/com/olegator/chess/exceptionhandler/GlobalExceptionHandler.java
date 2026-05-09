package com.olegator.chess.exceptionhandler;

import com.olegator.chess.exception.UserAlreadyExistsException;
import com.olegator.chess.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserConflict(UserAlreadyExistsException e) {
        return processException(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException e) {
        return processException(HttpStatus.UNAUTHORIZED, e);
    }

    private static ResponseEntity<Map<String, String>> processException(HttpStatus conflict, Exception e) {
        return ResponseEntity
                .status(conflict)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception e) {
        return processException(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
}
