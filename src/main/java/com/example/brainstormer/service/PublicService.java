package com.example.brainstormer.service;

import com.example.brainstormer.dto.CategoryDto;
import com.example.brainstormer.dto.TopicDto;
import com.example.brainstormer.dto.TopicExtendedDto;
import com.example.brainstormer.dto.UserDto;
import com.example.brainstormer.model.Category;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicService {

    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public List<UserDto> getUsers(String username) {
        return userRepository.findAllByUsernameContainingIgnoreCase(username).stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getCategories() {
        return Arrays.stream(Category.values())
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    public List<TopicDto> getPublicTopics() {
        return topicRepository.findAllByPublicVisibilityIsTrueOrderByCreatedAtDesc().stream()
                .map(TopicDto::new)
                .collect(Collectors.toList());
    }

    public TopicExtendedDto getPublicTopic(UUID id) {
        Topic topic = topicRepository.findById(id).orElseThrow();
        if (!topic.isPublicVisibility()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return new TopicExtendedDto(topic, true);
    }
}
