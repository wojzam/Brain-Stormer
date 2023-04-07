package com.example.brainstormer.controller;

import com.example.brainstormer.dto.IdeaCreateRequest;
import com.example.brainstormer.dto.IdeaDto;
import com.example.brainstormer.service.IdeaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/idea")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @PostMapping
    public ResponseEntity<IdeaDto> createIdea(@Valid @RequestBody IdeaCreateRequest request) {
        return ideaService.createIdea(request);
    }

    @PutMapping(path = "/{ideaId}")
    public void updateIdea(
            @PathVariable("ideaId") UUID id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        ideaService.updateIdea(id, title, description);
    }

    @DeleteMapping(path = "/{ideaId}")
    public void deleteIdea(@PathVariable("ideaId") UUID id) {
        ideaService.deleteIdea(id);
    }

    @PostMapping("/{ideaId}/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public void vote(@PathVariable("ideaId") UUID id, @Valid @Min(-1) @Max(1) @RequestParam short value) {
        ideaService.vote(id, value);
    }

    @DeleteMapping("/{ideaId}/vote")
    public void deleteVote(@PathVariable("ideaId") UUID id) {
        ideaService.deleteVote(id);
    }
}
