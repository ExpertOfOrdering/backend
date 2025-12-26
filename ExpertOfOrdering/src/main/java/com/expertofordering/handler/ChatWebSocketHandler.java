package com.expertofordering.handler;

import com.expertofordering.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final OpenAiService openAiService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("🔥 WebSocket CONNECTED: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String prompt = message.getPayload();
        System.out.println("📩 TEXT MESSAGE RECEIVED: " + prompt);

        // 비동기 AI 호출
        CompletableFuture
                .supplyAsync(() -> {
                    log.info("🧠 AI 요청 시작");
                    return openAiService.getChatResponse(prompt);
                })
                .thenAccept(response -> {
                    try {
                        log.info("📤 WS 전송");
                        session.sendMessage(new TextMessage(response));
                    } catch (Exception e) {
                        log.error("send error", e);
                    }
                })
                .exceptionally(e -> {
                    log.error("❌ AI 처리 중 오류", e);
                    try {
                        session.sendMessage(new TextMessage("⚠️ AI 오류 발생"));
                    } catch (Exception ignored) {}
                    return null;
                });
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: {} with status: {}", session.getId(), status);
    }
}
