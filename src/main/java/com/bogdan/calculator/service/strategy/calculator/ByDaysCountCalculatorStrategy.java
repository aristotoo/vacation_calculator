package com.bogdan.calculator.service.strategy.calculator;

import com.bogdan.calculator.aop.Loggable;
import com.bogdan.calculator.model.CalculationRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Strategy implementation for calculating vacation pay based on the number of vacation days.
 * This strategy calculates vacation pay by multiplying the daily rate by the number of vacation days.
 */
@Component
public class ByDaysCountCalculatorStrategy implements CalculatorStrategy {

    private static final BigDecimal AVERAGE_WORKING_DAYS_PER_MONTH = BigDecimal.valueOf(29.3);
    private static final int SCALE = 2;

    /**
     * Determines if this strategy can handle the given calculation request.
     * This strategy supports requests that specify the number of vacation days,
     * but do not specify start and end dates.
     *
     * @param request The calculation request to check
     * @return true if the request specifies the number of vacation days, false otherwise
     */
    @Override
    public boolean supports(CalculationRequest request) {
        boolean hasDays = request.getVacationDaysCount() != null;
        boolean hasStart = request.getStartVacation() != null;
        boolean hasEnd = request.getEndVacation() != null;

        return hasDays && !hasStart && !hasEnd;
    }

    /**
     * Calculates vacation pay based on the number of vacation days.
     * The calculation is done by multiplying the daily rate by the number of vacation days.
     * The daily rate is calculated by dividing the average salary by the average number of working days per month.
     *
     * @param request The calculation request containing average salary and number of vacation days
     * @return The calculated vacation pay amount
     */
    @Override
    @Loggable(value = "calculateSimpleVacation", logParams = true, logResult = true, logExecutionTime = true)
    public BigDecimal calculate(CalculationRequest request) {
        BigDecimal dailyRate = request.getAverageSalary()
                .divide(AVERAGE_WORKING_DAYS_PER_MONTH, SCALE, RoundingMode.HALF_UP);

        return dailyRate.multiply(BigDecimal.valueOf(request.getVacationDaysCount()))
                .setScale(SCALE, RoundingMode.HALF_UP);
    }
}
