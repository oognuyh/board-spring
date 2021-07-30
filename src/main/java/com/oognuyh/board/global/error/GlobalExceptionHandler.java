package com.oognuyh.board.global.error;

import java.util.HashMap;
import java.util.Map;

import com.oognuyh.board.post.web.exception.PostDoesNotExistException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> error = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors()
                .forEach(fieldError -> 
                    error.put(fieldError.getField(), fieldError.getDefaultMessage()));   
        }

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> error = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors()
                .forEach(fieldError -> 
                    error.put(fieldError.getField(), fieldError.getDefaultMessage()));   
        }

        System.out.println(error);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostDoesNotExistException.class)
    public ResponseEntity<?> handlePostDoesNotExistException(PostDoesNotExistException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}