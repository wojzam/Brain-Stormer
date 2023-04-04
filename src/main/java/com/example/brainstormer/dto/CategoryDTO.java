package com.example.brainstormer.dto;

import com.example.brainstormer.model.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private String name;
    private String label;

    public CategoryDTO(Category category) {
        name = category.name();
        label = category.toString();
    }
}
