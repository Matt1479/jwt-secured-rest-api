package com.matt1479.jwtsecuredrestapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.matt1479.jwtsecuredrestapi.users.UserRepository;

@Service 
public class AuthorizationService {
    
    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isOwner(Long userId) {
        Authentication authentication = getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // The following assumes JWT's subject is the username
        return userRepository.findById(userId)
            .map((user) -> user.getUsername()
                .equals(authentication.getName()))
            .orElse(false);
    }

    public boolean isOwner(String username) {
        Authentication authentication = getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return userRepository.findByUsername(username)
            .map((user) -> user.getUsername()
                .equals(authentication.getName()))
            .orElse(false);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
