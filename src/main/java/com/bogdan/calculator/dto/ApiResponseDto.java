package com.bogdan.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Data Transfer Object for API responses.
 * This class provides a standardized format for all API responses,
 * including success/failure status, messages, and data.
 *
 * @param <T> The type of data contained in the response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response")
public class ApiResponseDto<T> {
    @Schema(description = "Indicates if the request was successful", example = "true")
    private boolean success;
    
    @Schema(description = "Response message", example = "Success")
    private String message;
    
    @Schema(description = "HTTP status code", example = "200")
    private int statusCode;
    
    @Schema(description = "HTTP status name", example = "OK")
    private String status;
    
    @Schema(description = "Response data")
    private T data;

    /**
     * Creates a successful response with the specified data.
     *
     * @param data The data to include in the response
     * @param <T> The type of data
     * @return A successful API response
     */
    public static <T> ApiResponseDto<T> success(T data) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .message("Success")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(data)
                .build();
    }

    /**
     * Creates a successful response with the specified data and message.
     *
     * @param data The data to include in the response
     * @param message The success message
     * @param <T> The type of data
     * @return A successful API response
     */
    public static <T> ApiResponseDto<T> success(T data, String message) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.name())
                .data(data)
                .build();
    }

    /**
     * Creates an error response with the specified message and HTTP status.
     *
     * @param message The error message
     * @param status The HTTP status code
     * @param <T> The type of data (will be null in error responses)
     * @return An error API response
     */
    public static <T> ApiResponseDto<T> error(String message, HttpStatus status) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .message(message)
                .statusCode(status.value())
                .status(status.name())
                .data(null)
                .build();
    }
} 