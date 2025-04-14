package com.bogdan.calculator.service;

import com.bogdan.calculator.aop.Loggable;
import com.bogdan.calculator.exception.ApiException;
import com.bogdan.calculator.model.CalculationRequest;
import com.bogdan.calculator.service.strategy.calculator.CalculatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component responsible for resolving the appropriate calculation strategy
 * based on the provided calculation request.
 */
@Component
public class StrategyResolver {

    private final List<CalculatorStrategy> strategies;

    /**
     * Constructs a new StrategyResolver with the provided list of strategies.
     *
     * @param strategies List of available calculation strategies
     */
    @Autowired
    public StrategyResolver(List<CalculatorStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * Resolves and returns the appropriate calculation strategy for the given request.
     * The method logs the strategy selection process and its result.
     *
     * @param request The calculation request to find a strategy for
     * @return The appropriate calculation strategy
     * @throws ApiException if no suitable strategy is found for the request
     */
    @Loggable(value = "chooseStrategy", logParams = true, logResult = true, logExecutionTime = true)
    public CalculatorStrategy resolve(CalculationRequest request) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() ->
                        new ApiException("Invalid vacation request format or method of calculation not found",
                                HttpStatus.BAD_REQUEST));
    }
}

