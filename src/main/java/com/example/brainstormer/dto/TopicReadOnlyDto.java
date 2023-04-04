package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.Topic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TopicReadOnlyDto extends TopicDto {
    private boolean readOnly = true;
    private List<IdeaDto> ideas;
    private List<UserDto> collaborators;

    public TopicReadOnlyDto(Topic topic) {
        super(topic);

        this.ideas = topic.getIdeas().stream()
                .sorted(Comparator.comparing(Idea::getCreatedAt).reversed())
                .map(IdeaDto::new)
                .collect(Collectors.toList());

        this.collaborators = topic.getCollaborators().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }
}
