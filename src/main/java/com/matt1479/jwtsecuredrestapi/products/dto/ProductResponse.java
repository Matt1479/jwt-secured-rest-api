package com.matt1479.jwtsecuredrestapi.products.dto;

public record ProductResponse(
    Long id,
    String title,
    String description,
    Double price
) {}
