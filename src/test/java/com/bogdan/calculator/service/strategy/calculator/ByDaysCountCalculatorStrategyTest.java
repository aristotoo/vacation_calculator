package com.bogdan.calculator.service.strategy.calculator;

import com.bogdan.calculator.model.CalculationRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ByDaysCountCalculatorStrategyTest {

    private final ByDaysCountCalculatorStrategy strategy = new ByDaysCountCalculatorStrategy();

    @Test
    void supports_ReturnsTrueForValidRequest() {
        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .build();

        boolean result = strategy.supports(request);

        assertTrue(result);
    }

    @Test
    void supports_ReturnsFalseForInvalidRequest() {
        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .startVacation(LocalDate.now())
                .build();

        boolean result = strategy.supports(request);

        assertFalse(result);
    }

    @Test
    void calculate_ReturnsCorrectResult() {
        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .build();

        BigDecimal expected = new BigDecimal("17064.80");

        BigDecimal result = strategy.calculate(request);

        assertEquals(expected, result);
    }
} 