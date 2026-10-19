package com.matt1479.jwtsecuredrestapi.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
    @NotBlank
    @Size(max = 75) 
    String title,

    @NotBlank 
    @Size(max = 750)
    String description,

    @NotNull 
    Double price
) {}
