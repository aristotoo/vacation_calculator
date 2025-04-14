package com.bogdan.calculator.service;

import com.bogdan.calculator.dto.ApiResponseDto;
import com.bogdan.calculator.dto.CalculationRequestDto;

import java.math.BigDecimal;

public interface CalculationService {
    ApiResponseDto<BigDecimal> calculate(CalculationRequestDto requestDto);
}