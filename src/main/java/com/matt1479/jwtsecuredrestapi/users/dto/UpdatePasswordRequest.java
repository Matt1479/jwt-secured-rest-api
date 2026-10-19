package com.matt1479.jwtsecuredrestapi.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
    @NotBlank 
    @Size(min = 8, max = 20)
    String password
) {}
