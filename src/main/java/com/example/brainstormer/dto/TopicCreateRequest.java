package com.example.brainstormer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopicCreateRequest {

    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private Boolean publicVisibility;

    public TopicCreateRequest(String title, String description, Boolean publicVisibility) {
        this.title = title;
        this.description = description;
        this.publicVisibility = publicVisibility != null ? publicVisibility : false;
    }
}
