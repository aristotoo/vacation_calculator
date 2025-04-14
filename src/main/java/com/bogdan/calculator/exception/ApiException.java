package com.bogdan.calculator.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class for API-related errors.
 * This exception includes HTTP status code and error code information
 * for proper error handling and response generation.
 */
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    /**
     * Constructs a new API exception with the specified message and HTTP status.
     *
     * @param message The error message
     * @param status The HTTP status code
     */
    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = status.name();
    }

    /**
     * Constructs a new API exception with the specified message, HTTP status, and error code.
     *
     * @param message The error message
     * @param status The HTTP status code
     * @param errorCode The custom error code
     */
    public ApiException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
} 