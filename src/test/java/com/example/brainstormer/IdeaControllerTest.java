package com.example.brainstormer;

import com.example.brainstormer.dto.IdeaCreateRequest;
import com.example.brainstormer.dto.IdeaDTO;
import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.IdeaRepository;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.UserRepository;
import com.example.brainstormer.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.brainstormer.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IdeaControllerTest {

    private static final String PATH = "/idea";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Topic topic;
    private Idea idea;
    private String token;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(
                User.builder()
                        .username(USERNAME)
                        .email(EMAIL)
                        .password(passwordEncoder.encode(PASSWORD))
                        .role(Role.USER)
                        .build()
        );

        topic = topicRepository.save(
                Topic.builder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .publicVisibility(false)
                        .creator(user)
                        .build()
        );

        idea = ideaRepository.save(
                Idea.builder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .topic(topic)
                        .creator(user)
                        .build()
        );

        token = jwtService.generateToken(user);
    }

    private RequestBuilder requestWithJwtTokenHeader(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    private String ideaCreateRequest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new IdeaCreateRequest(topic.getId(), TITLE, DESCRIPTION));
    }

    @Test
    void shouldReturnOk_whenGettingIdea() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(get(PATH + "/" + idea.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFound_whenIdeaNotPresent() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(get(PATH + "/" + UUID.randomUUID())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreated_whenCreatingIdea() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ideaCreateRequest())))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnOk_whenUpdatingIdea() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(put(PATH + "/" + idea.getId())
                        .param("title", "New " + TITLE)
                        .param("description", "New " + DESCRIPTION)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenDeletingIdea() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH + "/" + idea.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "-1"})
    void shouldReturnCreated_whenVoting(String value) throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(post(PATH + "/" + idea.getId() + "/vote")
                        .param("value", value)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-2", "2", "10", "-10"})
    void shouldReturnBadRequest_whenVotingInvalidValue(String invalidValue) throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(post(PATH + "/" + idea.getId() + "/vote")
                        .param("value", invalidValue)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnCreated_whenDeletingVote() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH + "/" + idea.getId() + "/vote")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetIdea() throws Exception {
        IdeaDTO expectedIdea = new IdeaDTO(idea);

        mvc
                .perform(requestWithJwtTokenHeader(get(PATH + "/" + idea.getId())))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedIdea)));
    }

    @Test
    void shouldCreateIdea() throws Exception {
        MvcResult result = mvc
                .perform(requestWithJwtTokenHeader(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ideaCreateRequest())))
                .andDo(print())
                .andReturn();

        IdeaDTO createdIdea = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                IdeaDTO.class
        );
        assertTrue(ideaRepository.findById(createdIdea.getId()).isPresent());
    }

    @Test
    void shouldUpdateIdea() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(put(PATH + "/" + idea.getId())
                        .param("title", "New " + TITLE)
                        .param("description", "New " + DESCRIPTION)))
                .andDo(print());

        Idea updatedIdea = ideaRepository.findById(idea.getId()).orElseThrow();
        Assertions.assertEquals("New " + TITLE, updatedIdea.getTitle());
        Assertions.assertEquals("New " + DESCRIPTION, updatedIdea.getDescription());
    }

    @Test
    void shouldDeleteIdea() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH + "/" + idea.getId())))
                .andDo(print());

        assertTrue(ideaRepository.findById(idea.getId()).isEmpty());
    }
}
