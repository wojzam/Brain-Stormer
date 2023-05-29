package com.example.brainstormer.service;

import com.example.brainstormer.dto.TopicCreateRequest;
import com.example.brainstormer.dto.TopicDto;
import com.example.brainstormer.dto.TopicExtendedDto;
import com.example.brainstormer.dto.TopicReadOnlyDto;
import com.example.brainstormer.model.Category;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public List<TopicDto> getUserAccessibleTopics(String title) {
        User loggedInUser = authenticationService.getLoggedInUser();
        List<Topic> topics;

        if (title != null && title.trim().length() > 0) {
            topics = topicRepository.findAllByCreatorOrCollaboratorsAndTitle(loggedInUser, title);
        } else {
            topics = topicRepository.findAllByCreatorOrCollaborators(loggedInUser);
        }
        return topics.stream()
                .map(TopicDto::new)
                .collect(Collectors.toList());
    }

    public TopicDto getTopic(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Topic topic = topicRepository.findById(id).orElseThrow();

        if (topic.userAccessDenied(loggedInUser)) {
            if (!topic.isPublicVisibility()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            return new TopicReadOnlyDto(topic);
        }

        return new TopicExtendedDto(topic, loggedInUser);
    }

    public ResponseEntity<TopicDto> createTopic(TopicCreateRequest request) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Topic topic = Topic.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(Category.getCategoryByLabel(request.getCategory()))
                .publicVisibility(request.getPublicVisibility())
                .creator(loggedInUser)
                .build();

        for (String collaborator : request.getCollaborators()) {
            topic.addCollaborator(parseUserFromString(collaborator));
        }
        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TopicDto(topic));
    }

    @Transactional
    public void updateTopic(UUID id, String title, String description, String category, Boolean publicVisibility) {
        Topic topic = getLoggedInUserTopic(id);

        if (title != null && title.length() > 0) {
            topic.setTitle(title);
        }
        if (description != null) {
            topic.setDescription(description);
        }
        if (category != null) {
            topic.setCategory(Category.getCategoryByLabel(category));
        }
        if (publicVisibility != null) {
            topic.setPublicVisibility(publicVisibility);
        }
    }

    public void deleteTopic(UUID id) {
        topicRepository.deleteById(getLoggedInUserTopic(id).getId());
    }

    @Transactional
    public void addCollaborator(UUID topicId, String collaboratorId) {
        Topic topic = getLoggedInUserTopic(topicId);
        topic.addCollaborator(parseUserFromString(collaboratorId));
    }

    @Transactional
    public void removeCollaborator(UUID topicId, String collaboratorId) {
        Topic topic = getLoggedInUserTopic(topicId);
        User user = parseUserFromString(collaboratorId);
        if (!topic.getCollaborators().contains(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The collaborator could not be found");
        }

        topic.removeCollaborator(user);
    }

    private Topic getLoggedInUserTopic(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Topic topic = topicRepository.findById(id).orElseThrow();

        if (topic.getCreator() != loggedInUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return topic;
    }

    private User parseUserFromString(String userString) {
        try {
            UUID userId = UUID.fromString(userString);
            return userRepository.findById(userId).orElseThrow();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user UUID");
        }
    }
}
