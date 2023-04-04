package com.example.brainstormer.dto;

import com.example.brainstormer.model.Topic;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class TopicDto {
    protected UUID id;
    protected String title;
    protected String description;
    protected String category;
    protected boolean publicVisibility;

    public TopicDto(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
        this.category = topic.getCategory().toString();
        this.publicVisibility = topic.isPublicVisibility();
    }
}
