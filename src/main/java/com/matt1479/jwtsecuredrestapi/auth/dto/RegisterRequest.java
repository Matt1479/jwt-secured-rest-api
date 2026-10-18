package com.matt1479.jwtsecuredrestapi.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank 
    @Size(min = 3, max = 15)
    String username,

    @NotBlank 
    @Size(min = 8, max = 20)
    String password
) {}
