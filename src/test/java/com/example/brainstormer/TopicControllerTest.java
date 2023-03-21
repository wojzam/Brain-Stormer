package com.example.brainstormer;

import com.example.brainstormer.dto.TopicCreateRequest;
import com.example.brainstormer.dto.TopicDTO;
import com.example.brainstormer.dto.TopicExtendedDTO;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.UserRepository;
import com.example.brainstormer.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.List;
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
public class TopicControllerTest {

    private static final String PATH = "/topic";
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
    private PasswordEncoder passwordEncoder;
    private Topic topic;
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

        token = jwtService.generateToken(user);
    }

    private RequestBuilder requestWithJwtTokenHeader(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    private String topicCreateRequest() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new TopicCreateRequest(TITLE, DESCRIPTION, false));
    }

    @Test
    void shouldReturnOk_whenGettingUserAccessibleTopics() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(get(PATH)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenGettingUserAccessibleTopic() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(get(PATH + "/" + topic.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFound_whenTopicNotPresent() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(get(PATH + "/" + UUID.randomUUID())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreated_whenCreatingTopic() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(topicCreateRequest())))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnOk_whenUpdatingTopic() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(put(PATH + "/" + topic.getId())
                        .param("title", "New " + TITLE)
                        .param("description", "New " + DESCRIPTION)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenDeletingTopic() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH + "/" + topic.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUserAccessibleTopics() throws Exception {
        List<TopicDTO> expectedTopics = List.of(new TopicDTO(topic));

        mvc
                .perform(requestWithJwtTokenHeader(get(PATH)))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTopics)));
    }

    @Test
    void shouldGetUserAccessibleTopic() throws Exception {
        TopicExtendedDTO expectedTopic = new TopicExtendedDTO(topic);

        mvc
                .perform(requestWithJwtTokenHeader(get(PATH + "/" + topic.getId())))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTopic)));
    }

    @Test
    void shouldCreateTopic() throws Exception {
        MvcResult result = mvc
                .perform(requestWithJwtTokenHeader(post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(topicCreateRequest())))
                .andDo(print())
                .andReturn();

        TopicDTO createdTopic = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TopicDTO.class
        );
        assertTrue(topicRepository.findById(createdTopic.getId()).isPresent());
    }

    @Test
    void shouldUpdateTopic() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(put(PATH + "/" + topic.getId())
                        .param("title", "New " + TITLE)
                        .param("description", "New " + DESCRIPTION)))
                .andDo(print());

        Topic updatedTopic = topicRepository.findById(topic.getId()).orElseThrow();
        Assertions.assertEquals("New " + TITLE, updatedTopic.getTitle());
        Assertions.assertEquals("New " + DESCRIPTION, updatedTopic.getDescription());
    }

    @Test
    void shouldDeleteTopic() throws Exception {
        mvc
                .perform(requestWithJwtTokenHeader(delete(PATH + "/" + topic.getId())))
                .andDo(print());

        assertTrue(topicRepository.findById(topic.getId()).isEmpty());
    }
}
