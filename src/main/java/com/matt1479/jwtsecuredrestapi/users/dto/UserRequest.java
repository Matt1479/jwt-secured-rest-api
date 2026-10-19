package com.matt1479.jwtsecuredrestapi.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank 
    @Size(min = 3, max = 15)
    String username,

    @NotBlank 
    @Size(min = 8, max = 20)
    String password
) {}
