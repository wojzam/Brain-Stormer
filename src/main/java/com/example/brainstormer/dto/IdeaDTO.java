package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import lombok.Data;

import java.util.UUID;

@Data
public class IdeaDTO {
    private UUID id;
    private String title;
    private String description;
    private int votes;

    public IdeaDTO(Idea idea) {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.description = idea.getDescription();
        this.votes = idea.sumAllVotes();
    }
}