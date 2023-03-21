package com.example.brainstormer.dto;

import com.example.brainstormer.model.Topic;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TopicExtendedDTO {
    private UUID id;
    private String title;
    private String description;
    private boolean publicVisibility;
    private List<IdeaDTO> ideas;
    private List<UserDTO> collaborators;

    public TopicExtendedDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.publicVisibility = topic.isPublicVisibility();

        // TODO not working
//        this.ideas = topic.getIdeas().stream()
//                .map(IdeaDTO::new)
//                .collect(Collectors.toList());
//
//        this.collaborators = topic.getCollaborators().stream()
//                .map(UserDTO::new)
//                .collect(Collectors.toList());
    }
}