package com.example.brainstormer.controller;

import com.example.brainstormer.dto.CategoryDto;
import com.example.brainstormer.dto.TopicDto;
import com.example.brainstormer.dto.TopicExtendedDto;
import com.example.brainstormer.dto.UserDto;
import com.example.brainstormer.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicService publicService;

    @GetMapping("/user")
    public List<UserDto> getUsers(@RequestParam String username) {
        return publicService.getUsers(username);
    }

    @GetMapping("/category")
    public List<CategoryDto> getCategories() {
        return publicService.getCategories();
    }

    @GetMapping("/topic")
    public List<TopicDto> getPublicTopics() {
        return publicService.getPublicTopics();
    }

    @GetMapping(path = "/topic/{topicId}")
    public TopicExtendedDto getPublicTopic(@PathVariable("topicId") UUID id) {
        return publicService.getPublicTopic(id);
    }

}
