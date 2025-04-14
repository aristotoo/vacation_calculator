package com.bogdan.calculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VacationCalculatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Тестирование расчета по количеству дней:
     * запрос: averageSalary=50000, vacationDaysCount=10
     * расчет: dailyPay = 50000 / 29.3 (кругляем до 2 знаков) = 1706.48
     *         итог = 1706.48 * 10 = 17064.80
     */
    @Test
    void testCalculateByDays_Valid() throws Exception {
        BigDecimal expected = new BigDecimal("17064.80");

        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("vacationDaysCount", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(expected))//data.vacationPay
                .andExpect(jsonPath("$.message").value("Vacation pay calculated successfully"));
    }

    /**
     * Тестирование расчета отпускных за период времени:
     * Запрос: averageSalary=50000, startVacation=2025-04-29, endVacation=2025-05-14
     * Предполагаем, что в периоде 16 календарных дней 4 дня - выходные, и 2 дня праздника 1 мая и 9 мая.
     * Количество рабочих дней будет также 10.
     */
    @Test
    void testCalculateByPeriod_Valid() throws Exception {
        BigDecimal expected = new BigDecimal("17064.80");

        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("startVacation", "2025-04-29")
                        .param("endVacation", "2025-05-14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(expected))
                .andExpect(jsonPath("$.message").value("Vacation pay calculated successfully"));
    }

    /**
     * Тестирование невалидного запроса: передаются и количество дней отпуска и дата начала.
     * Ожидается выброс ApiException с сообщением "Invalid vacation request format or method of calculation not found"
     * которое должен перехватить GlobalExceptionHandler и вернуть статус 400
     */
    @Test
    void testInvalidRequest_DaysCountAndStartVacation() throws Exception {
        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("vacationDaysCount", "10")
                        .param("startVacation", "2025-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid vacation request format or method of calculation not found"));
    }

    /**
     * Тестирование невалидного запроса: передается только зарплата и дата начала отпуска.
     * Ожидается выброс ApiException с сообщением "Invalid vacation request format or method of calculation not found"
     * которое должен перехватить GlobalExceptionHandler и вернуть статус 400
     */
    @Test
    void testInvalidRequest_OnlyStartVacation() throws Exception {
        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("startVacation", "2025-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid vacation request format or method of calculation not found"));
    }

    /**
     * Тестирование невалидного запроса: передается только зарплата и дата окончания отпуска.
     * Ожидается выброс ApiException с сообщением "Invalid vacation request format or method of calculation not found"
     * которое должен перехватить GlobalExceptionHandler и вернуть статус 400
     */
    @Test
    void testInvalidRequest_OnlyEndVacation() throws Exception {
        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("endVacation", "2025-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid vacation request format or method of calculation not found"));
    }

    /**
     * Тестирование невалидного запроса: отсутствует обязательное поле зарплата,передается только количество дней отпуска.
     * Ожидается выброс BindException с сообщением "Validation error:Average salary is required"
     * которое должен перехватить GlobalExceptionHandler и вернуть статус 400
     */
    @Test
    void whenMissingAverageSalary_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/calculate")
                        .param("vacationDaysCount", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("averageSalary: Average salary is required"));
    }

    /**
     * Тестирование запроса при котором указана некоректная дата начала отпуска 2025-05-14 дата нала и
     * 2025-05-01 дата окончания отпуска.
     */
    @Test
    void whenInvalidDateRange_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("startVacation", "2025-05-14")
                        .param("endVacation", "2025-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("endVacation: End date must be after start date"));
    }

    /**
     * Тестирование запроса при котором общее число отпусткных дней больше максимального количества.
     * общее количсетво дней в данном тесте првышает 28 календарных дней.
     */
    @Test
    void whenVacationTooLong_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/calculate")
                        .param("averageSalary", "50000")
                        .param("startVacation", "2025-01-01")
                        .param("endVacation", "2025-02-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("endVacation: Vacation duration cannot exceed 28 days"));
    }
}
