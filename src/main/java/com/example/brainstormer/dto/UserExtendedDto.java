package com.example.brainstormer.dto;

import com.example.brainstormer.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserExtendedDto extends UserDto {
    private String email;
    private String role;

    public UserExtendedDto(User user) {
        super(user);
        this.email = user.getEmail();
        this.role = user.getRole().toString();
    }
}
