package com.bogdan.calculator.mapper;

import com.bogdan.calculator.dto.CalculationRequestDto;
import com.bogdan.calculator.model.CalculationRequest;
import org.springframework.stereotype.Component;

@Component
public class CalculationMapper {
    
    public CalculationRequest toModel(CalculationRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return CalculationRequest.builder()
                .averageSalary(dto.getAverageSalary())
                .vacationDaysCount(dto.getVacationDaysCount())
                .startVacation(dto.getStartVacation())
                .endVacation(dto.getEndVacation())
                .build();
    }
} 