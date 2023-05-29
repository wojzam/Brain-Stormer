package com.example.brainstormer.dto;

import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDto {

    protected UUID id;
    protected String username;
    protected boolean isAdmin;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.isAdmin = user.getRole().equals(Role.ADMIN);
    }
}
