package com.bogdan.calculator.exception;

import com.bogdan.calculator.dto.ApiResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleApiException_ReturnsCorrectResponse() {
        // Given
        String message = "Test error message";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException ex = new ApiException(message, status);

        // When
        ResponseEntity<ApiResponseDto<Void>> response = handler.handleApiException(ex);

        // Then
        assertNotNull(response);
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(message, response.getBody().getMessage());
        assertEquals(status.value(), response.getBody().getStatusCode());
        assertEquals(status.name(), response.getBody().getStatus());
    }

    @Test
    void handleValidationExceptions_ReturnsCorrectResponse() {
        // Given
        String fieldName = "averageSalary";
        String errorMessage = "must not be null";
        FieldError fieldError = new FieldError("objectName", fieldName, errorMessage);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, 
            new org.springframework.validation.BeanPropertyBindingResult(new Object(), "objectName"));
        ex.getBindingResult().addError(fieldError);

        // When
        ResponseEntity<ApiResponseDto<Void>> response = handler.handleValidationExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(fieldName + ": " + errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.name(), response.getBody().getStatus());
    }

    @Test
    void handleBindException_ReturnsCorrectResponse() {
        // Given
        String fieldName = "averageSalary";
        String errorMessage = "must not be null";
        FieldError fieldError = new FieldError("objectName", fieldName, errorMessage);
        BindException ex = new BindException(new Object(), "objectName");
        ex.addError(fieldError);

        // When
        ResponseEntity<ApiResponseDto<Void>> response = handler.handleBindException(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(fieldName + ": " + errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.name(), response.getBody().getStatus());
    }

    @Test
    void handleAllExceptions_ReturnsCorrectResponse() {
        // Given
        String message = "Test error message";
        Exception ex = new Exception(message);

        // When
        ResponseEntity<ApiResponseDto<Void>> response = handler.handleAllExceptions(ex);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("An unexpected error occurred: " + message, response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name(), response.getBody().getStatus());
    }
} 