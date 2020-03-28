package com.government.citizens.controllers.exceptions;

import com.government.citizens.exceptions.CitizenNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

}
