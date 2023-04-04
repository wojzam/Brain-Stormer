package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.User;
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
    private int userVote;

    public IdeaDto(Idea idea) {
        this.id = idea.getId();
        this.title = idea.getTitle();
        this.description = idea.getDescription();
        this.votes = 0;
        this.userVote = 0;
    }

    public IdeaDto(Idea idea, User user) {
        this(idea);
        this.votes = idea.sumAllVotes();
        this.userVote = idea.getUserVote(user);
    }
}
