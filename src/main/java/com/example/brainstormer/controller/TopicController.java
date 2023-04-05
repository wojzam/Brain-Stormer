package com.example.brainstormer.controller;

import com.example.brainstormer.dto.TopicCreateRequest;
import com.example.brainstormer.dto.TopicDto;
import com.example.brainstormer.service.TopicService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public List<TopicDto> getUserAccessibleTopics(@RequestParam(required = false) String title) {
        return topicService.getUserAccessibleTopics(title);
    }

    @GetMapping(path = "/{topicId}")
    public TopicDto getTopic(@PathVariable("topicId") UUID id) {
        return topicService.getTopic(id);
    }

    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody TopicCreateRequest request) {
        return topicService.createTopic(request);
    }

    @PutMapping(path = "/{topicId}")
    public void updateTopic(
            @PathVariable("topicId") UUID id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean publicVisibility
    ) {
        topicService.updateTopic(id, title, description, publicVisibility);
    }

    @DeleteMapping(path = "/{topicId}")
    public void deleteTopic(@PathVariable("topicId") UUID id) {
        topicService.deleteTopic(id);
    }

    @PostMapping("/{topicId}/collaborator")
    public void addCollaborator(@PathVariable("topicId") UUID id, @Valid @RequestBody CollaboratorRequest request) {
        topicService.addCollaborator(id, request.userId);
    }

    @DeleteMapping(path = "/{topicId}/collaborator")
    public void removeCollaborator(@PathVariable("topicId") UUID id, @Valid @RequestBody CollaboratorRequest request) {
        topicService.removeCollaborator(id, request.userId);
    }

    record CollaboratorRequest(@NotNull UUID userId) {
    }
}
