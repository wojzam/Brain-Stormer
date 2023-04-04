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
public class TopicExtendedDto {
    private UUID id;
    private String title;
    private String description;
    private String category;
    private boolean publicVisibility;
    private boolean readOnly;
    private List<IdeaDto> ideas;
    private List<UserDto> collaborators;

    public TopicExtendedDto(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.category = topic.getCategory().toString();
        this.publicVisibility = topic.isPublicVisibility();

        this.ideas = topic.getIdeas().stream()
                .sorted(Comparator.comparing(Idea::getCreatedAt).reversed())
                .map(IdeaDto::new)
                .collect(Collectors.toList());

        this.collaborators = topic.getCollaborators().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    public TopicExtendedDto(Topic topic, boolean readOnly) {
        this(topic);
        this.readOnly = readOnly;
    }
}
