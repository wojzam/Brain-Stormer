package com.example.brainstormer.controller;

import com.example.brainstormer.dto.UpdatePasswordRequest;
import com.example.brainstormer.dto.UserDto;
import com.example.brainstormer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDto getUser() {
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
        userService.updatePassword(request);
    }

    @DeleteMapping
    public void deleteUser() {
        userService.deleteUser();
    }
}
