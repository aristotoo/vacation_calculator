package com.bogdan.calculator.mapper;

import com.bogdan.calculator.dto.CalculationRequestDto;
import com.bogdan.calculator.model.CalculationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CalculationMapperTest {

    @Autowired
    private CalculationMapper mapper;

    @Test
    void toModel_ConvertsDtoToModel() {
        // Given
        CalculationRequestDto dto = CalculationRequestDto.builder()
                .averageSalary(new BigDecimal("50000"))
                .vacationDaysCount(10)
                .startVacation(LocalDate.of(2025, 4, 29))
                .endVacation(LocalDate.of(2025, 5, 14))
                .build();

        // When
        CalculationRequest model = mapper.toModel(dto);

        // Then
        assertNotNull(model);
        assertEquals(dto.getAverageSalary(), model.getAverageSalary());
        assertEquals(dto.getVacationDaysCount(), model.getVacationDaysCount());
        assertEquals(dto.getStartVacation(), model.getStartVacation());
        assertEquals(dto.getEndVacation(), model.getEndVacation());
    }
} 