package com.example.brainstormer.repository;

import com.example.brainstormer.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, UUID> {
}
