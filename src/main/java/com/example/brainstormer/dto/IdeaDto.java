package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class IdeaDto {
    private UUID id;
    private String title;
    private String description;
    private int votes;

    public IdeaDto(Idea idea) {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.description = idea.getDescription();
        this.votes = idea.sumAllVotes();
    }
}
