package com.matt1479.jwtsecuredrestapi.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.matt1479.jwtsecuredrestapi.auth.dto.LoginRequest;
import com.matt1479.jwtsecuredrestapi.auth.dto.RegisterRequest;
import com.matt1479.jwtsecuredrestapi.auth.dto.TokenResponse;
import com.matt1479.jwtsecuredrestapi.users.UserService;
import com.matt1479.jwtsecuredrestapi.users.dto.UserResponse;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthController(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );
        String accessToken = tokenService.generateToken(authentication);
        return new TokenResponse(accessToken, tokenService.getTtlSeconds());
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return userService.createUser(request.username(), request.password());
    }

}
