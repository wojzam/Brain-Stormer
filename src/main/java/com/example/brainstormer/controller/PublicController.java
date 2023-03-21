package com.example.brainstormer.controller;

import com.example.brainstormer.dto.TopicDTO;
import com.example.brainstormer.dto.TopicExtendedDTO;
import com.example.brainstormer.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicService publicService;

    @GetMapping("/topic")
    public List<TopicDTO> getPublicTopics() {
        return publicService.getPublicTopics();
    }

    @GetMapping(path = "/topic/{topicId}")
    public TopicExtendedDTO getPublicTopic(@PathVariable("topicId") UUID id) {
        return publicService.getPublicTopic(id);
    }

}
