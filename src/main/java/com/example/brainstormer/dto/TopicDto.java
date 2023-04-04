package com.example.brainstormer.dto;

import com.example.brainstormer.model.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TopicDto {
    private UUID id;
    private String title;
    private String description;
    private String categoryName;

    public TopicDto(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.categoryName = topic.getCategory().name();
    }
}
