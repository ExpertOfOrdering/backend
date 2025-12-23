package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Request to OpenAI Chat API")
public class ChatRequest {

    @Schema(description = "ID of the model to use", example = "gpt-3.5-turbo")
    private String model;

    @Schema(description = "A list of messages comprising the conversation so far.")
    private List<Message> messages;

    public ChatRequest(String model, String systemPrompt, String userPrompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("system", systemPrompt));
        this.messages.add(new Message("user", userPrompt));
    }

    @Data
    @Schema(description = "A single message in the chat conversation.")
    public static class Message {
        @Schema(description = "The role of the author of this message.", example = "user")
        private String role;

        @Schema(description = "The contents of the message.", example = "옵션 변경은 어떻게 하나요?")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
