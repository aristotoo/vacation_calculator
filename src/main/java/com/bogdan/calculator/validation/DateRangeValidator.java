package com.bogdan.calculator.validation;

import com.bogdan.calculator.dto.CalculationRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Validator for checking date range constraints in vacation calculation requests.
 * This validator ensures that:
 * 1. The end date is after the start date
 * 2. The vacation duration does not exceed the maximum allowed days
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, CalculationRequestDto> {
    
    private static final int MAX_VACATION_DAYS = 28;

    /**
     * Validates the date range in the calculation request.
     * The validation is skipped if either start or end date is not provided.
     *
     * @param request The calculation request to validate
     * @param context The validation context
     * @return true if the date range is valid, false otherwise
     */
    @Override
    public boolean isValid(CalculationRequestDto request, ConstraintValidatorContext context) {
        LocalDate start = request.getStartVacation();
        LocalDate end = request.getEndVacation();

        // Если даты не предоставлены - пропускаем валидацию
        if (start == null || end == null) {
            return true;
        }

        boolean isValid = true;
        
        // Проверка корректности диапазона дат
        if (end.isBefore(start)) {
            String message = "End date must be after start date";
            String field = "endVacation";
            createCustomErrorMessage(context, message, field);
            isValid = false;
        }

        // Проверка максимальной продолжительности отпуска
        if (isValid) {
            long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
            if (totalDays > MAX_VACATION_DAYS) {
                String message = "Vacation duration cannot exceed " + MAX_VACATION_DAYS + " days";
                String field = "endVacation";
                createCustomErrorMessage(context, message, field);
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Creates a custom error message for a specific field.
     *
     * @param context The validation context
     * @param message The error message
     * @param field The field name
     */
    private void createCustomErrorMessage(ConstraintValidatorContext context, String message, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
} 