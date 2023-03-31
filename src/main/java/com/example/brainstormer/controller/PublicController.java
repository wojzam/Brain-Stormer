package com.example.brainstormer.controller;

import com.example.brainstormer.dto.TopicDTO;
import com.example.brainstormer.dto.TopicExtendedDTO;
import com.example.brainstormer.dto.UserDTO;
import com.example.brainstormer.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicService publicService;

    @GetMapping("/user")
    public List<UserDTO> getUsers(@RequestParam String username) {
        return publicService.getUsers(username);
    }

    @GetMapping("/category")
    public LinkedHashMap<String, String> getCategories() {
        return publicService.getCategoriesNames();
    }

    @GetMapping("/topic")
    public List<TopicDTO> getPublicTopics() {
        return publicService.getPublicTopics();
    }

    @GetMapping(path = "/topic/{topicId}")
    public TopicExtendedDTO getPublicTopic(@PathVariable("topicId") UUID id) {
        return publicService.getPublicTopic(id);
    }

}
