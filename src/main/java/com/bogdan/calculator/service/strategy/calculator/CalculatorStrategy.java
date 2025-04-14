package com.bogdan.calculator.service.strategy.calculator;

import com.bogdan.calculator.model.CalculationRequest;

import java.math.BigDecimal;

/**
 * Interface defining the contract for vacation pay calculation strategies.
 * Implementations of this interface provide different ways to calculate vacation pay
 * based on the provided calculation request.
 */
public interface CalculatorStrategy {
    /**
     * Calculates the vacation pay based on the provided request.
     *
     * @param request The calculation request containing necessary data for calculation
     * @return The calculated vacation pay amount
     */
    BigDecimal calculate(CalculationRequest request);

    /**
     * Determines if this strategy can handle the given calculation request.
     *
     * @param request The calculation request to check
     * @return true if this strategy can handle the request, false otherwise
     */
    boolean supports(CalculationRequest request);
}
