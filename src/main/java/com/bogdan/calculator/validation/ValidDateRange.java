package com.bogdan.calculator.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Annotation for validating date ranges in vacation calculation requests.
 * This annotation is used to ensure that:
 * 1. The end date is after the start date
 * 2. The vacation duration does not exceed the maximum allowed days
 */
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    /**
     * The error message to display when validation fails.
     *
     * @return The error message
     */
    String message() default "End date must be after start date";

    /**
     * The validation groups this constraint belongs to.
     *
     * @return The validation groups
     */
    Class<?>[] groups() default {};

    /**
     * The payload associated with the constraint.
     *
     * @return The payload
     */
    Class<? extends Payload>[] payload() default {};
} 