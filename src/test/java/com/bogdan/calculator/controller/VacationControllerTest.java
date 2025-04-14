package com.bogdan.calculator.controller;

import com.bogdan.calculator.dto.ApiResponseDto;
import com.bogdan.calculator.dto.CalculationRequestDto;
import com.bogdan.calculator.service.CalculationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VacationControllerTest {

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private VacationController vacationController;

    @Test
    void calculate_ReturnsCorrectResponse() {
        // Given
        CalculationRequestDto requestDto = CalculationRequestDto.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .build();

        BigDecimal result = new BigDecimal("17064.80");
        ApiResponseDto<BigDecimal> serviceResponse = ApiResponseDto.success(result, "Vacation pay calculated successfully");

        when(calculationService.calculate(any(CalculationRequestDto.class))).thenReturn(serviceResponse);

        // When
        ResponseEntity<ApiResponseDto<BigDecimal>> response = vacationController.calculate(requestDto);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Vacation pay calculated successfully", response.getBody().getMessage());
        assertEquals(result, response.getBody().getData());
    }
} 