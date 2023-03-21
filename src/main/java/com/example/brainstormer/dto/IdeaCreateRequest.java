package com.example.brainstormer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdeaCreateRequest {

    @NotNull(message = "TopicId is required")
    private UUID topicId;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
}