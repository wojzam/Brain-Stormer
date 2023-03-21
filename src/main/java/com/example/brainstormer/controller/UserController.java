package com.example.brainstormer.controller;

import com.example.brainstormer.dto.UserDTO;
import com.example.brainstormer.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDTO getUser() {
        return userService.getUser();
    }

    @PutMapping
    public void updateUser(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        // TODO validate new email
        userService.updateUser(username, email);
    }

    @PostMapping("/updatePassword")
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(request.oldPassword, request.newPassword);
    }

    @DeleteMapping
    public void deleteUser() {
        userService.deleteUser();
    }

    record UpdatePasswordRequest(
            @NotBlank(message = "Old password is required")
            String oldPassword,

            @NotBlank(message = "New password is required")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String newPassword
    ) {
    }
}
