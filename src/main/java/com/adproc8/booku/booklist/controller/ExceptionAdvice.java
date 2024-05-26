package com.adproc8.booku.booklist.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.NOT_FOUND, "Resource not found");
        return errorResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.BAD_REQUEST, "Invalid request body");
        return errorResponse;
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleRestClientException(RestClientException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.INTERNAL_SERVER_ERROR, "Failed to call external service");
        return errorResponse;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.CONFLICT, "Data already exists in the database");
        return errorResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.BAD_REQUEST, "Invalid request");
        return errorResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorResponse handleRuntimeException(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.INTERNAL_SERVER_ERROR, "An error occured on the server side");
        return errorResponse;
    }
}
