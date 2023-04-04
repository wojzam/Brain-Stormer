package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TopicExtendedDTO {
    private UUID id;
    private String title;
    private String description;
    private String categoryName;
    private boolean publicVisibility;
    private boolean previewOnly;
    private List<IdeaDTO> ideas;
    private List<UserDTO> collaborators;

    public TopicExtendedDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.categoryName = topic.getCategory().name();
        this.publicVisibility = topic.isPublicVisibility();

        this.ideas = topic.getIdeas().stream()
                .sorted(Comparator.comparing(Idea::getCreatedAt).reversed())
                .map(IdeaDTO::new)
                .collect(Collectors.toList());

        this.collaborators = topic.getCollaborators().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public TopicExtendedDTO(Topic topic, boolean previewOnly) {
        this(topic);
        this.previewOnly = previewOnly;
    }
}
