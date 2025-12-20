package com.expertofordering.controller;

import com.expertofordering.service.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat", description = "OpenAI Chatbot endpoints")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiService openAiService;

    @Operation(summary = "Get Chatbot Response", description = "Send a prompt to the chatbot and get a response.")
    @PostMapping
    public String getChatResponse(@RequestBody String prompt) {
        return openAiService.getChatResponse(prompt);
    }
}
