package com.bogdan.calculator.exception;

import com.bogdan.calculator.dto.ApiResponseDto;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * This class handles various types of exceptions and converts them into appropriate API responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles custom API exceptions and converts them into appropriate API responses.
     *
     * @param ex The API exception to handle
     * @return ResponseEntity containing the error response
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleApiException(ApiException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponseDto.error(ex.getMessage(), ex.getStatus()));
    }

    /**
     * Handles validation exceptions from method arguments and converts them into appropriate API responses.
     *
     * @param ex The validation exception to handle
     * @return ResponseEntity containing the error response with validation messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = getErrorMessage(ex.getBindingResult());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(errorMessage, HttpStatus.BAD_REQUEST));
    }

    /**
     * Extracts error messages from the binding result.
     *
     * @param ex The binding result containing validation errors
     * @return A formatted string containing all validation error messages
     */
    private String getErrorMessage(BindingResult ex) {
        return ex.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError) {
                        FieldError fieldError = (FieldError) error;
                        return fieldError.getField() + ": " + error.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .collect(Collectors.joining(", "));
    }

    /**
     * Handles constraint violation exceptions and converts them into appropriate API responses.
     *
     * @param ex The constraint violation exception to handle
     * @return ResponseEntity containing the error response with constraint violation messages
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(errorMessage, HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles bind exceptions and converts them into appropriate API responses.
     *
     * @param ex The bind exception to handle
     * @return ResponseEntity containing the error response with binding error messages
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleBindException(BindException ex) {
        String errorMessage = getErrorMessage(ex.getBindingResult());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.error(errorMessage, HttpStatus.BAD_REQUEST));
    }

    /**
     * Handles all other unhandled exceptions and converts them into appropriate API responses.
     *
     * @param ex The exception to handle
     * @return ResponseEntity containing the error response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> handleAllExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDto.error("An unexpected error occurred: " + ex.getMessage(), 
                                      HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
