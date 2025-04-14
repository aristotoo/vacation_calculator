package com.bogdan.calculator.service.strategy.calculator;

import com.bogdan.calculator.aop.Loggable;
import com.bogdan.calculator.model.CalculationRequest;
import com.bogdan.calculator.service.strategy.provider.HolidayProviderStrategy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Strategy implementation for calculating vacation pay based on a specific period.
 * This strategy calculates vacation pay by considering working days within the specified period,
 * excluding weekends and holidays.
 */
@Component
public class ByPeriodCalculatorStrategy implements CalculatorStrategy {

    private static final BigDecimal AVERAGE_WORKING_DAYS_PER_MONTH = BigDecimal.valueOf(29.3);
    private static final int SCALE = 2;


    private static final Set<DayOfWeek> WEEKENDS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private final HolidayProviderStrategy holidayProvider;
    private final Map<LocalDate, Boolean> workingDayCache = new HashMap<>();

    /**
     * Constructs a new ByPeriodCalculatorStrategy with the provided holiday provider.
     *
     * @param holidayProvider The provider for holiday dates
     */
    public ByPeriodCalculatorStrategy(HolidayProviderStrategy holidayProvider) {
        this.holidayProvider = holidayProvider;
    }

    /**
     * Determines if this strategy can handle the given calculation request.
     * This strategy supports requests that specify start and end dates of vacation,
     * but do not specify the number of vacation days.
     *
     * @param request The calculation request to check
     * @return true if the request specifies start and end dates, false otherwise
     */
    @Override
    public boolean supports(CalculationRequest request) {
        boolean hasDays = request.getVacationDaysCount() != null;
        boolean hasStart = request.getStartVacation() != null;
        boolean hasEnd = request.getEndVacation() != null;

        return !hasDays && hasStart && hasEnd;
    }

    /**
     * Calculates vacation pay based on the specified period.
     * The calculation considers only working days (excluding weekends and holidays)
     * within the specified period.
     * The result is cached based on the average salary and vacation dates.
     *
     * @param request The calculation request containing average salary and vacation dates
     * @return The calculated vacation pay amount
     */
    @Override
    @Cacheable(value = "vacationCalculations", key = "{#request.averageSalary, #request.startVacation, #request.endVacation}")
    @Loggable(value = "calculateDetailedVacation", logParams = true, logResult = true, logExecutionTime = true)
    public BigDecimal calculate(CalculationRequest request) {
        List<LocalDate> vacationDays = generateVacationDays(request);
        Set<LocalDate> holidays = holidayProvider.getAllHoliday();

        long paidDays = vacationDays.stream()
                .filter(date -> isWorkingDay(date, holidays))
                .count();

        BigDecimal dailyPay = request.getAverageSalary()
                .divide(AVERAGE_WORKING_DAYS_PER_MONTH, SCALE, RoundingMode.HALF_UP);

        return dailyPay.multiply(BigDecimal.valueOf(paidDays)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Generates a list of all dates within the specified vacation period.
     *
     * @param request The calculation request containing start and end dates
     * @return List of dates within the vacation period
     */
    private List<LocalDate> generateVacationDays(CalculationRequest request) {
        LocalDate start = request.getStartVacation();
        LocalDate end = request.getEndVacation();
        long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
        return Stream.iterate(start, date -> date.plusDays(1))
                .limit(totalDays)
                .collect(Collectors.toList());
    }

    /**
     * Determines if a given date is a working day.
     * The result is cached to improve performance.
     *
     * @param date The date to check
     * @param holidays Set of holiday dates
     * @return true if the date is a working day, false otherwise
     */
    private boolean isWorkingDay(LocalDate date, Set<LocalDate> holidays) {
        return workingDayCache.computeIfAbsent(date, day -> 
            !WEEKENDS.contains(day.getDayOfWeek()) && !holidays.contains(day)
        );
    }
}
