package com.example.brainstormer.service;

import com.example.brainstormer.dto.TopicDTO;
import com.example.brainstormer.dto.TopicExtendedDTO;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicService {

    private final TopicRepository topicRepository;

    public List<TopicDTO> getPublicTopics() {
        return topicRepository.findAllByPublicVisibilityIsTrueOrderByCreatedAtDesc().stream()
                .map(TopicDTO::new)
                .collect(Collectors.toList());
    }

    public TopicExtendedDTO getPublicTopic(UUID id) {
        Topic topic = topicRepository.findById(id).orElseThrow();
        if (!topic.isPublicVisibility()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return new TopicExtendedDTO(topic);
    }

}
