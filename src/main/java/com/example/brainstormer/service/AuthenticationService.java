package com.example.brainstormer.service;

import com.example.brainstormer.dto.AuthenticationRequest;
import com.example.brainstormer.dto.AuthenticationResponse;
import com.example.brainstormer.dto.RegisterRequest;
import com.example.brainstormer.exception.ConflictException;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new ConflictException("Provided username is already in use");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Provided email is already in use");
        }

        repository.save(user);
        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername()).orElseThrow();
        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return repository.findByUsername(authentication.getName());
        }
        return Optional.empty();
    }
}
