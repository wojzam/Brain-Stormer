package com.example.brainstormer.repository;

import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TopicRepository extends JpaRepository<Topic, UUID> {

    @Query("SELECT t FROM Topic t WHERE (t.creator = :user OR :user MEMBER OF t.collaborators) ORDER BY t.createdAt DESC")
    List<Topic> findAllByCreatorOrCollaborators(User user);

    @Query("SELECT t FROM Topic t WHERE (t.creator = :user OR :user MEMBER OF t.collaborators) AND LOWER(t.title) LIKE %:title% ORDER BY t.createdAt DESC")
    List<Topic> findAllByCreatorOrCollaboratorsAndTitle(User user, String title);

    List<Topic> findAllByPublicVisibilityIsTrueOrderByCreatedAtDesc();

    List<Topic> findAllByPublicVisibilityIsTrueAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
}
