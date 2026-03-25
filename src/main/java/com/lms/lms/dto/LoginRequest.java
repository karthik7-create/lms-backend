// LeaveRequestDto.java
package com.lms.lms.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
public record LeaveRequestDto(
        @NotNull @FutureOrPresent LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotBlank String reason
) {}
