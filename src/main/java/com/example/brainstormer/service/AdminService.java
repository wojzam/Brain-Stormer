package com.example.brainstormer.service;

import com.example.brainstormer.dto.UserDto;
import com.example.brainstormer.dto.UserExtendedDto;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserDto> getUsers(String username) {
        List<User> userList = (username == null || username.isBlank()) ?
                userRepository.findAll() :
                userRepository.findAllByUsernameContainingIgnoreCase(username);

        return userList.stream()
                .map(UserExtendedDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateUserRole(UUID id, String role) {
        User user = userRepository.findById(id).orElseThrow();
        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unrecognized role");

        }
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

}
