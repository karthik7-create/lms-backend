package com.lms.lms.dto;

package com.leavemgmt.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String password,
        Long managerId
) {}
