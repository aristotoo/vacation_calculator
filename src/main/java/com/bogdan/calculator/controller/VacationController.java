package com.bogdan.calculator.controller;

import com.bogdan.calculator.dto.ApiResponseDto;
import com.bogdan.calculator.dto.CalculationRequestDto;
import com.bogdan.calculator.service.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * Controller for handling vacation pay calculation requests.
 * This controller provides endpoints for calculating vacation pay based on
 * average salary and either vacation days count or vacation period dates.
 */
@RestController
@RequestMapping("/api/v1")
@Validated
@Tag(name = "Vacation Calculator", description = "API for calculating vacation pay")
public class VacationController {

    private final CalculationService calculationService;

    public VacationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    /**
     * Calculates vacation pay based on the provided request parameters.
     * The calculation can be done either by specifying the number of vacation days
     * or by providing a vacation period (start and end dates).
     *
     * @param request The calculation request containing average salary and either
     *               vacation days count or vacation period dates
     * @return ResponseEntity containing the calculated vacation pay amount
     */
    @GetMapping("/calculate")
    @Operation(
        summary = "Calculate vacation pay",
        description = "Calculates vacation pay based on average salary and either vacation days count or vacation period",
        responses = {
            @ApiResponse(responseCode = "200", description = "Vacation pay calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    public ResponseEntity<ApiResponseDto<BigDecimal>> calculate(
        @Parameter(description = "Calculation request parameters", required = true)
        @ModelAttribute @Valid CalculationRequestDto request) {
        ApiResponseDto<BigDecimal> calculate = calculationService.calculate(request);
        return ResponseEntity.ok(calculate);
    }
}
