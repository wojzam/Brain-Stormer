package com.example.brainstormer.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum Category {
    BUSINESS("Business"),
    EDUCATION("Education"),
    LIFESTYLE("Lifestyle"),
    SCIENCE_AND_TECHNOLOGY("Science and Technology"),
    HEALTH_AND_MEDICINE("Health and Medicine"),
    ARTS_AND_CULTURE("Arts and Culture"),
    SPORTS_AND_RECREATION("Sports and Recreation"),
    TRAVEL_AND_TOURISM("Travel and Tourism"),
    SOCIAL_ISSUES_AND_ETHICS("Social Issues and Ethics"),
    POLITICS_AND_GOVERNMENT("Politics and Government"),
    HISTORY_AND_HERITAGE("History and Heritage"),
    FOOD_AND_BEVERAGE("Food and Beverage"),
    FASHION_AND_STYLE("Fashion and Style"),
    HOME_AND_DESIGN("Home and Design"),
    PERSONAL_DEVELOPMENT_AND_SELF_IMPROVEMENT("Personal Development and Self-Improvement");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public static Category parseCategoryName(String categoryName) {
        try {
            return Category.valueOf(categoryName);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unrecognized topic category name");
        }
    }

    @Override
    public String toString() {
        return label;
    }
}
