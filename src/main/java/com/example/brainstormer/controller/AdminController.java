package com.example.brainstormer.controller;

import com.example.brainstormer.dto.UserDto;
import com.example.brainstormer.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/user")
    public List<UserDto> getUsers(@RequestParam(required = false) String username) {
        return adminService.getUsers(username);
    }

    @PutMapping(path = "/user/{userId}")
    public void updateUserRole(@PathVariable("userId") UUID id, @RequestParam String role) {
        adminService.updateUserRole(id, role);
    }

    @DeleteMapping(path = "/user/{userId}")
    public void deleteUser(@PathVariable("userId") UUID id) {
        adminService.deleteUser(id);
    }

}
