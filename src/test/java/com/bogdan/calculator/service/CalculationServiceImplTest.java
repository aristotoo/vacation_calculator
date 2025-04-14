package com.bogdan.calculator.service;

import com.bogdan.calculator.dto.ApiResponseDto;
import com.bogdan.calculator.dto.CalculationRequestDto;
import com.bogdan.calculator.mapper.CalculationMapper;
import com.bogdan.calculator.model.CalculationRequest;
import com.bogdan.calculator.service.strategy.calculator.CalculatorStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculationServiceImplTest {

    @Mock
    private StrategyResolver resolver;

    @Mock
    private CalculationMapper mapper;

    @Mock
    private CalculatorStrategy strategy;

    @InjectMocks
    private CalculationServiceImpl calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new CalculationServiceImpl(resolver);
        calculationService.setMapper(mapper);
    }

    @Test
    void calculate_ReturnsCorrectResponse() {
        // Given
        CalculationRequestDto requestDto = CalculationRequestDto.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .build();

        CalculationRequest request = CalculationRequest.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .build();

        BigDecimal result = new BigDecimal("17064.80");
        ApiResponseDto<BigDecimal> expectedResponse = ApiResponseDto.success(result, "Vacation pay calculated successfully");

        when(mapper.toModel(requestDto)).thenReturn(request);
        when(resolver.resolve(request)).thenReturn(strategy);
        when(strategy.calculate(request)).thenReturn(result);

        // When
        ApiResponseDto<BigDecimal> actualResponse = calculationService.calculate(requestDto);

        // Then
        assertEquals(expectedResponse.getData(), actualResponse.getData());
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.isSuccess(), actualResponse.isSuccess());
    }
} 