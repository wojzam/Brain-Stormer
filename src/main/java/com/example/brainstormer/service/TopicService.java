package com.example.brainstormer.service;

import com.example.brainstormer.dto.TopicCreateRequest;
import com.example.brainstormer.dto.TopicDTO;
import com.example.brainstormer.dto.TopicExtendedDTO;
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

    public List<TopicDTO> getUserAccessibleTopics() {
        User loggedInUser = authenticationService.getLoggedInUser();
        return topicRepository.findAllByCreatorOrCollaboratorsContainingOrderByCreatedAtDesc(
                        loggedInUser,
                        loggedInUser
                ).stream()
                .map(TopicDTO::new)
                .collect(Collectors.toList());
    }

    public TopicExtendedDTO getUserAccessibleTopic(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Topic topic = topicRepository.findById(id).orElseThrow();
        if (topic.userAccessDenied(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return new TopicExtendedDTO(topic);
    }

    public ResponseEntity<TopicDTO> createTopic(TopicCreateRequest request) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Topic topic = Topic.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(Category.parseCategoryName(request.getCategoryName()))
                .publicVisibility(request.getPublicVisibility())
                .creator(loggedInUser)
                .build();

        topicRepository.save(topic);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TopicDTO(topic));
    }

    @Transactional
    public void updateTopic(UUID id, String title, String description, Boolean publicVisibility) {
        Topic topic = getLoggedInUserTopic(id);

        if (title != null && title.length() > 0) {
            topic.setTitle(title);
        }
        if (description != null) {
            topic.setDescription(description);
        }
        if (publicVisibility != null) {
            topic.setPublicVisibility(publicVisibility);
        }
    }

    public void deleteTopic(UUID id) {
        topicRepository.deleteById(getLoggedInUserTopic(id).getId());
    }

    @Transactional
    public void addCollaborator(UUID topicId, UUID collaboratorId) {
        Topic topic = getLoggedInUserTopic(topicId);
        User user = userRepository.findById(collaboratorId).orElseThrow();

        topic.addCollaborator(user);
    }

    @Transactional
    public void removeCollaborator(UUID topicId, UUID collaboratorId) {
        Topic topic = getLoggedInUserTopic(topicId);
        User user = userRepository.findById(collaboratorId).orElseThrow();
        if (!topic.getCollaborators().contains(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
}
