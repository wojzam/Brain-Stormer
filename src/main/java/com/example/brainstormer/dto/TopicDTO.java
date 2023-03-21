package com.example.brainstormer.dto;

import com.example.brainstormer.model.Topic;
import lombok.Data;

import java.util.UUID;

@Data
public class TopicDTO {
    private UUID id;
    private String title;
    private String description;

    public TopicDTO(Topic topic) {
        this.id = topic.getId();
        this.title = topic.getTitle();
        this.description = topic.getDescription();
    }
}
