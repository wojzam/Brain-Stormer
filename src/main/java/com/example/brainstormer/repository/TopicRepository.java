package com.example.brainstormer.repository;

import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {

    List<Topic> findAllByCreatorOrCollaboratorsContainingOrderByCreatedAtDesc(User creator, User collaborator);

    List<Topic> findAllByPublicVisibilityIsTrueOrderByCreatedAtDesc();
}
