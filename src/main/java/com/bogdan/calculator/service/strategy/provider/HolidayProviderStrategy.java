package com.bogdan.calculator.service.strategy.provider;


import java.time.LocalDate;
import java.util.Set;


/**
 * Interface defining the contract for holiday data providers.
 * Implementations of this interface provide access to holiday dates
 * that are used in vacation pay calculations.
 */
public interface HolidayProviderStrategy {
    /**
     * Retrieves all holiday dates.
     *
     * @return A set of holiday dates
     */
    Set<LocalDate> getAllHoliday();
}
