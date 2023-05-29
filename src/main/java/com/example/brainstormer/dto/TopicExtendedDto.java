package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TopicExtendedDto extends TopicDto {
    private List<IdeaDto> ideas;
    private List<UserDto> collaborators;
    private boolean canEdit;

    public TopicExtendedDto(Topic topic, User user) {
        super(topic);
        this.ideas = topic.getIdeas().stream()
                .sorted(Comparator.comparing(Idea::getCreatedAt).reversed())
                .map(idea -> new IdeaDto(idea, user))
                .collect(Collectors.toList());

        this.collaborators = topic.getCollaborators().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());

        this.canEdit = topic.getCreator() == user;
    }
}
