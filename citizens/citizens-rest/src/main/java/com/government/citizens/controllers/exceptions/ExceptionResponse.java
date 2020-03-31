package com.government.citizens.controllers.exceptions;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

/**
 * @author Andrii Sysoiev
 */
public class ExceptionResponse {
    private final LocalDateTime dateTime;
    private String message;
    private String details;

    public ExceptionResponse() {
        this.dateTime = now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public ExceptionResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDetails() {
        return details;
    }

    public ExceptionResponse setDetails(String details) {
        this.details = details;
        return this;
    }
}
