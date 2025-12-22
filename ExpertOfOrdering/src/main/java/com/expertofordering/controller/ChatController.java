package com.expertofordering.controller;

import com.expertofordering.service.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat", description = "OpenAI 챗봇 API")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final OpenAiService openAiService;

    @Operation(
            summary = "Get Chatbot Response",
            description = "챗봇에게 질문을 합니다. <br><br> **Note:** 헤더에 JWT토큰  'Authorization: Bearer {token}' 이 형태로 넣어야 합니다!",
            requestBody = @RequestBody(
                    description = "User's question for the chatbot.",
                    required = true,
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = "옵션 변경은 어떻게 하나요?")
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully received a response from the chatbot.",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string"),
                    examples = @ExampleObject(value = "주문 목록에서 변경하고 싶은 메뉴를 선택하면 옵션을 다시 고를 수 있는 화면으로 이동합니다.")
            )
    )
    @PostMapping
    public String getChatResponse(@org.springframework.web.bind.annotation.RequestBody String prompt) {
        return openAiService.getChatResponse(prompt);
    }
}
