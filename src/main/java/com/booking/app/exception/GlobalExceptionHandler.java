package com.booking.app.exception;

import com.booking.app.annotation.UserNotConfirmedException;
import com.booking.app.exception.exception.PasswordNotMatchesException;
import com.booking.app.exception.exception.ResourceNotFoundException;
import com.booking.app.exception.exception.UserAlreadyExistAuthenticationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import java.util.List;
import java.util.StringJoiner;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest webRequest) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (FieldError fieldError : fieldErrors) {
            stringJoiner.add(fieldError.getDefaultMessage());
        }
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                stringJoiner.toString(), // Set the error messages in your error details
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorDetails, status);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> notFound(ResourceNotFoundException exception, WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
    public ResponseEntity<ErrorDetails> userAlreadyExist(UserAlreadyExistAuthenticationException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotMatchesException.class)
    public ResponseEntity<ErrorDetails> passwordMatches(PasswordNotMatchesException exception, WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserNotConfirmedException.class)
    public ResponseEntity<ErrorDetails> passwordMatches(UserNotConfirmedException exception, WebRequest webRequest) {

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }


}
