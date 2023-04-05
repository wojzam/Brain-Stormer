package com.example.brainstormer.controller;

import com.example.brainstormer.dto.TopicDto;
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
    public List<String> getCategories() {
        return publicService.getCategories();
    }

    @GetMapping("/topic")
    public List<TopicDto> getPublicTopics(@RequestParam(required = false) String title) {
        return publicService.getPublicTopics(title);
    }

    @GetMapping(path = "/topic/{topicId}")
    public TopicDto getPublicTopic(@PathVariable("topicId") UUID id) {
        return publicService.getPublicTopic(id);
    }

}
