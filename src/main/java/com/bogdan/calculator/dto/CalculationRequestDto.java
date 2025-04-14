package com.bogdan.calculator.dto;

import com.bogdan.calculator.validation.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for vacation calculation requests.
 * This class represents the input data required for calculating vacation pay,
 * including average salary and either vacation days count or vacation period dates.
 */
@Data
@Builder
@ValidDateRange
@Schema(description = "Request for vacation pay calculation")
public class CalculationRequestDto {
    /**
     * The average monthly salary for vacation pay calculation.
     * Must be greater than 0.
     */
    @NotNull(message = "Average salary is required")
    @DecimalMin(value = "0.01", message = "Average salary can be greater than 0")
    @Schema(description = "Average monthly salary", example = "50000.00", required = true)
    private BigDecimal averageSalary;
    
    /**
     * The number of vacation days.
     * Must be 0 or greater.
     * Either this field or startVacation and endVacation must be provided.
     */
    @Min(value = 0, message = "Amount vacation days cannot be less than zero")
    @Schema(description = "Number of vacation days", example = "14")
    private Integer vacationDaysCount;
    
    /**
     * The start date of the vacation period.
     * Must be in yyyy-MM-dd format.
     * Either this field and endVacation or vacationDaysCount must be provided.
     */
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Start date of vacation period in yyyy-MM-dd format", example = "2024-01-01")
    private LocalDate startVacation;
    
    /**
     * The end date of the vacation period.
     * Must be in yyyy-MM-dd format.
     * Must be after startVacation and the period must not exceed 28 days.
     * Either this field and startVacation or vacationDaysCount must be provided.
     */
    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "End date of vacation period in yyyy-MM-dd format", example = "2024-01-14")
    private LocalDate endVacation;
} 