package com.example.brainstormer;

import com.example.brainstormer.dto.AuthenticationRequest;
import com.example.brainstormer.dto.RegisterRequest;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.example.brainstormer.TestConstants.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerTest {

    private static final String REGISTER_PATH = "/auth/register";
    private static final String AUTHENTICATE_PATH = "/auth/authenticate";
    private static String registerRequest;
    private static String authenticationRequest;
    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();

        registerRequest = objectMapper.writeValueAsString(
                new RegisterRequest(USERNAME, EMAIL, PASSWORD)
        );
        authenticationRequest = objectMapper.writeValueAsString(
                new AuthenticationRequest(USERNAME, PASSWORD)
        );
    }

    private void addDefaultUser() {
        userRepository.save(User.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.USER)
                .build()
        );
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andDo(print());

        assertTrue(userRepository.findByUsername(USERNAME).isPresent());
    }

    @Test
    public void shouldReturnCreated_whenRegistrationIsSuccessful() throws Exception {
        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnOk_whenAuthenticationIsSuccessful() throws Exception {
        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andDo(print());

        mvc
                .perform(post(AUTHENTICATE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequest))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnJWTToken_whenRegistrationIsSuccessful() throws Exception {
        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andDo(print()).andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void shouldReturnJWTToken_whenAuthenticationIsSuccessful() throws Exception {
        addDefaultUser();

        mvc
                .perform(post(AUTHENTICATE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequest))
                .andDo(print()).andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void shouldReturnConflict_whenUsernameIsTaken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(USERNAME, "unique@email.com", PASSWORD);
        String conflictRequestJson = objectMapper.writeValueAsString(registerRequest);
        addDefaultUser();

        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conflictRequestJson))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsStringIgnoringCase("username")));
    }

    @Test
    public void shouldReturnConflict_whenEmailIsTaken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("uniqueUsername", EMAIL, PASSWORD);
        String conflictRequestJson = objectMapper.writeValueAsString(registerRequest);
        addDefaultUser();

        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conflictRequestJson))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsStringIgnoringCase("email")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "email", "password"})
    public void shouldReturnBadRequest_whenRegisterRequestPropertyIsNull(String nullKey) throws Exception {
        JsonNode rootNode = objectMapper.readTree(registerRequest);
        ((ObjectNode) rootNode).remove(nullKey);
        String incompleteRequest = objectMapper.writeValueAsString(rootNode);

        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteRequest))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsStringIgnoringCase(nullKey)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "email", "password"})
    public void shouldReturnBadRequest_whenRegisterPropertyIsEmpty(String emptyKey) throws Exception {
        JsonNode rootNode = objectMapper.readTree(registerRequest);
        ((ObjectNode) rootNode).put(emptyKey, "");
        String incompleteRequest = objectMapper.writeValueAsString(rootNode);

        mvc
                .perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteRequest))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsStringIgnoringCase(emptyKey)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "password"})
    public void shouldReturnBadRequest_whenAuthenticationRequestPropertyIsNull(String nullKey) throws Exception {
        JsonNode rootNode = objectMapper.readTree(authenticationRequest);
        ((ObjectNode) rootNode).remove(nullKey);
        String incompleteRequest = objectMapper.writeValueAsString(rootNode);

        mvc
                .perform(post(AUTHENTICATE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteRequest))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsStringIgnoringCase(nullKey)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username", "password"})
    public void shouldReturnBadRequest_whenAuthenticationPropertyIsEmpty(String emptyKey) throws Exception {
        JsonNode rootNode = objectMapper.readTree(authenticationRequest);
        ((ObjectNode) rootNode).put(emptyKey, "");
        String incompleteRequest = objectMapper.writeValueAsString(rootNode);

        mvc
                .perform(post(AUTHENTICATE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(incompleteRequest))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsStringIgnoringCase(emptyKey)));
    }
}

