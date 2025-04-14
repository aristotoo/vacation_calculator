package com.bogdan.calculator.service.strategy.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of HolidayProviderStrategy that reads holiday dates from a file.
 * The holiday dates are loaded during initialization and cached for subsequent requests.
 */
@Component
@Slf4j
public class FileHolidayProviderStrategy implements HolidayProviderStrategy {

    private static final Path HOLIDAYS_PATH = Paths.get("src/main/resources/holidays.csv");
    private final Set<LocalDate> holidays = new HashSet<>();

    /**
     * Initializes the holiday provider by reading holiday dates from the configured file.
     * The dates are parsed and stored in memory for quick access.
     */
    @PostConstruct
    public void init() {
        try (BufferedReader reader = Files.newBufferedReader(HOLIDAYS_PATH)) {
            reader.lines()
                    .map(LocalDate::parse)
                    .forEach(holidays::add);
        } catch (IOException ex) {
            log.error("Error processing holiday data", ex);
        }
    }

    /**
     * Retrieves all holiday dates from the cache.
     * The result is cached to improve performance.
     *
     * @return An unmodifiable set of holiday dates
     */
    @Override
    @Cacheable(value = "holidays", sync = true)
    public Set<LocalDate> getAllHoliday() {
        return Collections.unmodifiableSet(holidays);
    }
}