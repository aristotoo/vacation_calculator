package com.bogdan.calculator.service;

import com.bogdan.calculator.aop.Loggable;
import com.bogdan.calculator.dto.ApiResponseDto;
import com.bogdan.calculator.dto.CalculationRequestDto;
import com.bogdan.calculator.mapper.CalculationMapper;
import com.bogdan.calculator.model.CalculationRequest;
import com.bogdan.calculator.service.strategy.calculator.CalculatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service implementation for vacation pay calculations.
 * This service handles the calculation process by delegating to appropriate strategies
 * and mapping between DTOs and domain models.
 */
@Service
public class CalculationServiceImpl implements CalculationService {
    private final StrategyResolver resolver;
    private CalculationMapper mapper;

    /**
     * Sets the mapper for converting between DTOs and domain models.
     *
     * @param mapper The mapper to be used
     */
    @Autowired
    public void setMapper(CalculationMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Constructs a new CalculationServiceImpl with the provided strategy resolver.
     *
     * @param resolver The strategy resolver to be used
     */
    @Autowired
    public CalculationServiceImpl(StrategyResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Calculates vacation pay based on the provided request DTO.
     * The method logs the calculation process, including parameters and result.
     *
     * @param requestDto The calculation request DTO
     * @return API response containing the calculated vacation pay
     */
    @Override
    @Loggable(value = "calculateVacation", logParams = true, logResult = true, logExecutionTime = true)
    public ApiResponseDto<BigDecimal> calculate(CalculationRequestDto requestDto) {
        CalculationRequest request = mapper.toModel(requestDto);
        CalculatorStrategy strategy = resolver.resolve(request);
        BigDecimal result = strategy.calculate(request);
        return ApiResponseDto.success(result, "Vacation pay calculated successfully");
    }
}

