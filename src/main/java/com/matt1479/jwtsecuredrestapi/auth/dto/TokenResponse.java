package com.matt1479.jwtsecuredrestapi.auth.dto;

public record TokenResponse(String accessToken, long expiresInSeconds) {}
