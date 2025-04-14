package com.bogdan.calculator.service.strategy.calculator;

import com.bogdan.calculator.model.CalculationRequest;
import com.bogdan.calculator.service.strategy.provider.HolidayProviderStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ByPeriodCalculatorStrategyTest {

    @Mock
    private HolidayProviderStrategy holidayProvider;

    private ByPeriodCalculatorStrategy strategy;

    @Test
    void supports_ReturnsTrueForValidRequest() {
        strategy = new ByPeriodCalculatorStrategy(holidayProvider);
        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .startVacation(LocalDate.of(2025, 4, 29))
                .endVacation(LocalDate.of(2025, 5, 14))
                .build();

        boolean result = strategy.supports(request);

        assertTrue(result);
    }

    @Test
    void supports_ReturnsFalseForInvalidRequest() {
        strategy = new ByPeriodCalculatorStrategy(holidayProvider);
        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .startVacation(LocalDate.of(2025, 4, 29))
                .endVacation(LocalDate.of(2025, 5, 14))
                .build();

        boolean result = strategy.supports(request);

        assertFalse(result);
    }

    @Test
    void calculate_ReturnsCorrectResult() {
        strategy = new ByPeriodCalculatorStrategy(holidayProvider);
        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .startVacation(LocalDate.of(2025, 4, 29))
                .endVacation(LocalDate.of(2025, 5, 14))
                .build();

        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2025, 5, 1)); // 1 мая
        holidays.add(LocalDate.of(2025, 5, 9)); // 9 мая

        when(holidayProvider.getAllHoliday()).thenReturn(holidays);

        BigDecimal expected = new BigDecimal("17064.80");

        BigDecimal result = strategy.calculate(request);

        assertEquals(expected, result);
    }
} 