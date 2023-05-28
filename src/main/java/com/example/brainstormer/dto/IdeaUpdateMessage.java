package com.example.brainstormer.dto;

import com.example.brainstormer.model.Idea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdeaUpdateMessage {

    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String VOTE = "VOTE";
    public static final String DELETE = "DELETE";
    private UUID topicId;
    private UUID userId;
    private IdeaDto idea;
    private String action;

    private static IdeaUpdateMessage createMessage(Idea idea, UUID userId, String action) {
        return IdeaUpdateMessage.builder()
                .topicId(idea.getTopic().getId())
                .userId(userId)
                .idea(new IdeaDto(idea))
                .action(action)
                .build();
    }

    public static IdeaUpdateMessage getCreateOf(Idea idea, UUID userId) {
        return createMessage(idea, userId, CREATE);
    }

    public static IdeaUpdateMessage getUpdateOf(Idea idea, UUID userId) {
        return createMessage(idea, userId, UPDATE);
    }

    public static IdeaUpdateMessage getVoteOf(Idea idea, UUID userId, int voteChange) {
        IdeaUpdateMessage message = createMessage(idea, userId, VOTE);
        message.getIdea().setVotes(voteChange);
        return message;
    }

    public static IdeaUpdateMessage getDeleteOf(Idea idea, UUID userId) {
        return createMessage(idea, userId, DELETE);
    }
}
