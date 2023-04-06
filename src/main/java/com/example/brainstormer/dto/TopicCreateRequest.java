package com.example.brainstormer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class TopicCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotBlank(message = "Category is required")
    private String category;
    private Boolean publicVisibility;
    private Set<String> collaborators;

    public TopicCreateRequest(String title, String description, String category, Boolean publicVisibility, Set<String> collaborators) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.publicVisibility = publicVisibility != null ? publicVisibility : false;
        this.collaborators = collaborators;
    }
}
