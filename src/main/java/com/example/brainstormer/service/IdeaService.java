package com.example.brainstormer.service;

import com.example.brainstormer.dto.IdeaCreateRequest;
import com.example.brainstormer.dto.IdeaDto;
import com.example.brainstormer.dto.IdeaUpdateMessage;
import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.model.Vote;
import com.example.brainstormer.repository.IdeaRepository;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final Logger logger = LoggerFactory.getLogger(IdeaService.class);
    private final IdeaRepository ideaRepository;
    private final TopicRepository topicRepository;
    private final VoteRepository voteRepository;
    private final AuthenticationService authenticationService;
    private final KafkaTemplate<String, IdeaUpdateMessage> kafkaTemplate;

    public ResponseEntity<IdeaDto> createIdea(IdeaCreateRequest request) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Topic topic = topicRepository.findById(request.getTopicId()).orElseThrow();

        if (topic.userAccessDenied(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Idea idea = Idea.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .topic(topic)
                .creator(loggedInUser)
                .build();

        ideaRepository.save(idea);
        sendMessage(IdeaUpdateMessage.getCreateOf(idea, loggedInUser.getId()));
        IdeaDto ideaDto = new IdeaDto(idea);
        ideaDto.setCanEdit(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(ideaDto);
    }

    @Transactional
    public void updateIdea(UUID id, String title, String description) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = getUserIdea(loggedInUser, id);

        if (title != null && title.length() > 0) {
            idea.setTitle(title);
        }
        if (description != null) {
            idea.setDescription(description);
        }

        sendMessage(IdeaUpdateMessage.getUpdateOf(idea, loggedInUser.getId()));
    }

    public void deleteIdea(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = getUserIdea(loggedInUser, id);
        ideaRepository.deleteById(idea.getId());
        sendMessage(IdeaUpdateMessage.getDeleteOf(idea, loggedInUser.getId()));
    }

    @Transactional
    public void vote(UUID id, short value) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = ideaRepository.findById(id).orElseThrow();

        if (idea.getTopic().userAccessDenied(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        int previousVoteValue = idea.getUserVote(loggedInUser);
        idea.getVotes().removeIf(vote -> vote.getUser().equals(loggedInUser));

        Vote vote = Vote.builder()
                .user(loggedInUser)
                .idea(idea)
                .value(value)
                .build();

        idea.getVotes().add(vote);
        voteRepository.save(vote);
        sendMessage(IdeaUpdateMessage.getVoteOf(idea, loggedInUser.getId(), value - previousVoteValue));
    }

    @Transactional
    public void deleteVote(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = ideaRepository.findById(id).orElseThrow();

        if (idea.getTopic().userAccessDenied(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        int previousVoteValue = idea.getUserVote(loggedInUser);

        voteRepository.deleteAllByUserAndIdea(loggedInUser, idea);
        sendMessage(IdeaUpdateMessage.getVoteOf(idea, loggedInUser.getId(), -previousVoteValue));
    }

    private Idea getUserIdea(User user, UUID ideaId) {
        Idea idea = ideaRepository.findById(ideaId).orElseThrow();

        if (idea.getCreator() != user) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return idea;
    }

    private void sendMessage(IdeaUpdateMessage ideaUpdateMessage) {
        CompletableFuture<SendResult<String, IdeaUpdateMessage>> future = kafkaTemplate.send("idea-updates", ideaUpdateMessage);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.warn("Unable to send message due to : " + ex.getMessage());
            }
        });
    }
}
