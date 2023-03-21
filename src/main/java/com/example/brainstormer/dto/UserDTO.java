package com.example.brainstormer.dto;

import com.example.brainstormer.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDTO {

    private UUID id;
    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
