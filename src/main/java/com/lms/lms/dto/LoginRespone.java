package com.lms.lms.dto;


public record LoginResponse(
        String token, Long userId, String fullName, String email, String userType
) {}