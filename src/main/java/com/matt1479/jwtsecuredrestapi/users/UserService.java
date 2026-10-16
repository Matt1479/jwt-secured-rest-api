package com.matt1479.jwtsecuredrestapi.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.matt1479.jwtsecuredrestapi.users.dto.UserResponse;

@Service 
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional 
    public UserResponse createUser(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Username is already taken");
        }

        // TODO: use password encoder to encode the password
        User user = new User(username, rawPassword, Set.of(Role.USER));

        // TODO: handle race condition
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"));
        
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        List<UserResponse> users = new ArrayList<>();

        userRepository.findAll()
            .forEach((user) -> users.add(toResponse(user)));

        return users;
    }

    @Transactional 
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(), user.getUsername(), user.getRoles());
    }

}
