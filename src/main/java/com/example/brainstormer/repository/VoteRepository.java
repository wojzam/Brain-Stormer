package com.example.brainstormer.repository;

import com.example.brainstormer.model.Idea;
import com.example.brainstormer.model.User;
import com.example.brainstormer.model.Vote;
import com.example.brainstormer.model.VoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, VoteId> {

    void deleteAllByUserAndIdea(User user, Idea idea);
}
