package com.government.citizens.controllers.exceptions;

import com.government.citizens.exceptions.CitizenNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Andrii Sysoiev
 */
@ControllerAdvice
@RestController
public class CitizensExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CitizenNotFoundException.class)
    public final ResponseEntity handleUserNotFoundException(CitizenNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse()
                .setMessage(ex.getMessage())
                .setDetails(request.getDescription(false));
        return new ResponseEntity(exceptionResponse, NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity handleValidationException(ValidationException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse()
                .setMessage("Validation error!")
                .setDetails(ex.getMessage());
        return new ResponseEntity(exceptionResponse, BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse()
                .setMessage("Validation error!")
                .setDetails(ex.getBindingResult().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse()
                .setMessage("Validation error!")
                .setDetails(ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
