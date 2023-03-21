package com.example.brainstormer.service;

import com.example.brainstormer.dto.UpdatePasswordRequest;
import com.example.brainstormer.dto.UserDTO;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO getUser() {
        return new UserDTO(authenticationService.getLoggedInUser());
    }

    @Transactional
    public void updateUser(String username, String email) {
        User user = authenticationService.getLoggedInUser();

        if (username != null && username.length() > 0 && !user.getUsername().equals(username)) {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Provided username is already in use");
            }
            user.setUsername(username);
        }
        if (email != null && email.length() > 0 && !user.getEmail().equals(email)) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Provided email is already in use");
            }
            user.setEmail(email);
        }
    }

    @Transactional
    public void updatePassword(UpdatePasswordRequest request) {
        User user = authenticationService.getLoggedInUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Provided old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    public void deleteUser() {
        User user = authenticationService.getLoggedInUser();
        userRepository.delete(user);
    }
}
