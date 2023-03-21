package com.example.brainstormer.service;

import com.example.brainstormer.dto.IdeaCreateRequest;
import com.example.brainstormer.dto.IdeaDTO;
import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.model.Vote;
import com.example.brainstormer.repository.IdeaRepository;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final TopicRepository topicRepository;
    private final VoteRepository voteRepository;
    private final AuthenticationService authenticationService;

    public ResponseEntity<IdeaDTO> createIdea(IdeaCreateRequest request) {
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
        return ResponseEntity.status(HttpStatus.CREATED).body(new IdeaDTO(idea));
    }

    public IdeaDTO getIdea(UUID id) {
        return new IdeaDTO(ideaRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void updateIdea(UUID id, String title, String description) {
        Idea idea = getLoggedInUserIdea(id);

        if (title != null && title.length() > 0) {
            idea.setTitle(title);
        }
        if (description != null) {
            idea.setDescription(description);
        }
    }

    public void deleteIdea(UUID id) {
        ideaRepository.deleteById(getLoggedInUserIdea(id).getId());
    }

    @Transactional
    public void vote(UUID id, short value) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = ideaRepository.findById(id).orElseThrow();

        if (idea.getTopic().userAccessDenied(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Vote vote = Vote.builder()
                .user(loggedInUser)
                .idea(idea)
                .value(value)
                .build();

        Set<Vote> voteSet = Set.of(vote);
        idea.setVotes(voteSet);
        voteRepository.save(vote);
    }

    @Transactional
    public void deleteVote(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = ideaRepository.findById(id).orElseThrow();

        if (idea.getTopic().userAccessDenied(loggedInUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        voteRepository.deleteAllByUserAndIdea(loggedInUser, idea);
    }

    private Idea getLoggedInUserIdea(UUID id) {
        User loggedInUser = authenticationService.getLoggedInUser();
        Idea idea = ideaRepository.findById(id).orElseThrow();

        if (idea.getCreator() != loggedInUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return idea;
    }
}
