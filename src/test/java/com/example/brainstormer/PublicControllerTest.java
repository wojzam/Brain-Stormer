package com.example.brainstormer;

import com.example.brainstormer.dto.TopicDTO;
import com.example.brainstormer.dto.TopicExtendedDTO;
import com.example.brainstormer.model.Role;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.example.brainstormer.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PublicControllerTest {

    private static final String PATH = "/public/topic";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Topic topic;
    private User user;

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

        topic = topicRepository.save(
                Topic.builder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .category(CATEGORY)
                        .publicVisibility(true)
                        .creator(user)
                        .build()
        );
    }

    @Test
    void shouldReturnOk_whenGettingPublicTopics() throws Exception {
        mvc
                .perform(get(PATH))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOk_whenGettingPublicTopicById() throws Exception {
        mvc
                .perform(get(PATH + "/" + topic.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFound_whenTopicNotPresent() throws Exception {
        mvc
                .perform(get(PATH + "/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnForbidden_whenTopicIsPrivate() throws Exception {
        Topic privateTopic = topicRepository.save(
                Topic.builder()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .publicVisibility(false)
                        .creator(user)
                        .build()
        );

        mvc
                .perform(get(PATH + "/" + privateTopic.getId()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetPublicTopics() throws Exception {
        List<TopicDTO> expectedTopics = List.of(new TopicDTO(topic));

        MvcResult result = mvc
                .perform(get(PATH))
                .andDo(print())
                .andReturn();

        List<TopicDTO> actualTopics = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        Assertions.assertTrue(actualTopics.containsAll(expectedTopics));
    }

    @Test
    void shouldGetPublicTopic() throws Exception {
        TopicExtendedDTO expectedTopic = new TopicExtendedDTO(topic, true);

        mvc
                .perform(get(PATH + "/" + topic.getId()))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTopic)));
    }
}
