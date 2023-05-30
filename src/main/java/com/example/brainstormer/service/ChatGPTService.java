package com.example.brainstormer.service;

import com.example.brainstormer.dto.IdeaCreateRequest;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.websocket.IdeaGenerateStatusHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private static final Duration TIMEOUT = Duration.ofSeconds(60);
    private final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IdeaService ideaService;
    private final IdeaGenerateStatusHandler statusHandler;
    private OpenAiService service;
    @Value("${spring.openai.token}")
    private String token;

    @PostConstruct
    private void initialize() {
        if (token == null || token.isBlank()) {
            service = null;
            logger.warn("Failed ChatGPTService initialization - OPENAI_TOKEN variable is not set!");
        } else{
            service = new OpenAiService(token, TIMEOUT);
            logger.info("ChatGPTService initialized successfully");
        }
    }

    public void createGenerateIdeasAsyncTask(Topic topic, User user) {
        if (service == null || statusHandler.containsTask(topic.getId())) {
            return;
        }
        statusHandler.addGenerationTask(topic.getId(), generateIdeasAsync(topic, user));
        logger.info("Added new chatGPT idea generation task");
    }

    @Async
    protected CompletableFuture<String> generateIdeasAsync(Topic topic, User user) {
        return CompletableFuture.supplyAsync(() -> ask(generatePrompt(topic)))
                .thenApply(response -> {
                    logger.info("Received response from chatGPT: " + response);
                    List<IdeaCreateRequest> ideaCreateRequests = convertResponseToIdeaCreateRequests(topic.getId(), response);
                    for (IdeaCreateRequest ideaCreateRequest : ideaCreateRequests) {
                        // the sessionUserId is set to random by chatGPT to allow all users receive update messages
                        ideaService.createIdea(ideaCreateRequest, user, UUID.randomUUID());
                    }
                    statusHandler.removeCompletedTask(topic.getId());

                    return response;
                });
    }

    private String generatePrompt(Topic topic) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Give me 5 ideas for this topic: ").append(topic.getTitle());

        if (topic.getDescription() != null) {
            stringBuilder.append(" - ").append(topic.getDescription());
        }
        stringBuilder.append(".The topic category is ").append(topic.getCategory());
        stringBuilder.append(".Write your response in json array format with ideas. Each idea should be json object with two keys: title and description.");
        return stringBuilder.toString();
    }

    private String ask(String prompt) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .messages(List.of(new ChatMessage("user", prompt)))
                .model("gpt-3.5-turbo")
                .build();
        List<ChatCompletionChoice> choices = service.createChatCompletion(request).getChoices();
        StringBuilder stringBuilder = new StringBuilder();

        choices.stream()
                .map(ChatCompletionChoice::getMessage)
                .map(ChatMessage::getContent)
                .forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    private List<IdeaCreateRequest> convertResponseToIdeaCreateRequests(UUID topicId, String response) {
        List<IdeaCreateRequest> ideaList = new ArrayList<>();
        response = response.replaceAll(".*?(\\[.*?]).*", "$1");

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            for (JsonNode element : jsonNode) {
                ideaList.add(new IdeaCreateRequest(
                        topicId,
                        element.get("title").asText(),
                        element.get("description").asText()
                ));
            }
        } catch (Exception ignored) {
        }
        return ideaList;
    }

}
