package com.matt1479.jwtsecuredrestapi.users.dto;

import java.util.Set;
import com.matt1479.jwtsecuredrestapi.users.Role;

public record UserResponse(Long id, String username, Set<Role> roles) {}
