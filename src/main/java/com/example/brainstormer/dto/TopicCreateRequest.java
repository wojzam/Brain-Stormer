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
    @NotBlank(message = "Category name is required")
    private String categoryName;
    private Boolean publicVisibility;

    public TopicCreateRequest(String title, String description, String categoryName, Boolean publicVisibility) {
        this.title = title;
        this.description = description;
        this.categoryName = categoryName.toUpperCase();
        this.publicVisibility = publicVisibility != null ? publicVisibility : false;
    }
}
