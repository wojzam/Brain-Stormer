package com.example.brainstormer;

import com.example.brainstormer.dto.UpdatePasswordRequest;
import com.example.brainstormer.dto.UserDTO;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.UserRepository;
import com.example.brainstormer.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static com.example.brainstormer.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    private static final String PATH = "/user";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                User.builder()
                        .username(USERNAME)
                        .email(EMAIL)
                        .password(passwordEncoder.encode(PASSWORD))
                        .role(Role.USER)
                        .build()
        );

        token = jwtService.generateToken(user);
    }

    private RequestBuilder requestWithJwtTokenHeader(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    @Test
    void shouldReturnOk_whenGettingUser() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(get(PATH)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenUpdatingUser() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(put(PATH)
                        .param("username", "newUsername")
                        .param("email", "new@email.com")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenUpdatingUserPassword() throws Exception {
        String newPassword = "NEW" + PASSWORD;
        String updatePasswordRequest = objectMapper.writeValueAsString(
                new UpdatePasswordRequest(PASSWORD, newPassword));

        mvc
                .perform(requestWithJwtTokenHeader(post(PATH + "/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenDeletingUser() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUser() throws Exception {
        UserDTO expectedUser = new UserDTO(user);

        mvc
                .perform(requestWithJwtTokenHeader(get(PATH)))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(put(PATH)
                        .param("username", "newUsername")
                        .param("email", "new@email.com")))
                .andDo(print());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("new@email.com", updatedUser.getEmail());
    }

    @Test
    void shouldUpdateUserPassword() throws Exception {
        String newPassword = "NEW" + PASSWORD;
        String updatePasswordRequest = objectMapper.writeValueAsString(
                new UpdatePasswordRequest(PASSWORD, newPassword));

        mvc
                .perform(requestWithJwtTokenHeader(post(PATH + "/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePasswordRequest)))
                .andDo(print());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }

    @Test
    void shouldDeletingUser() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH)))
                .andDo(print());

        assertTrue(userRepository.findById(user.getId()).isEmpty());
    }

}
