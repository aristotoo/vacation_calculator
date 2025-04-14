package com.bogdan.calculator.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain model for vacation calculation requests.
 * This class represents the internal model used for vacation pay calculations,
 * containing the same fields as CalculationRequestDto but without validation annotations.
 */
@Data
@Builder
public class CalculationRequest {
    /**
     * The average monthly salary for vacation pay calculation.
     */
    private BigDecimal averageSalary;

    /**
     * The number of vacation days.
     */
    private Integer vacationDaysCount;

    /**
     * The start date of the vacation period.
     * Must be in yyyy-MM-dd format.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startVacation;

    /**
     * The end date of the vacation period.
     * Must be in yyyy-MM-dd format.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endVacation;
}

