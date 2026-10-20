package com.matt1479.jwtsecuredrestapi.users;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matt1479.jwtsecuredrestapi.users.dto.UpdatePasswordRequest;
import com.matt1479.jwtsecuredrestapi.users.dto.UserRequest;
import com.matt1479.jwtsecuredrestapi.users.dto.UserResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping 
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request.username(), request.password());
    }

    @PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#id)")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable @Min(1) Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#username)")
    @GetMapping(params = {"username"})
    public UserResponse getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"!username"})
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("@authorizationService.isOwner(#id)")
    @PatchMapping("/{id}/updatePassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(id, request.password());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable @Min(1) Long id) {
        userService.deleteUserById(id);
    }

}
