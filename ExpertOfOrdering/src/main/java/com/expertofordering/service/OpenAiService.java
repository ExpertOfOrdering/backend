package com.expertofordering.service;

import com.expertofordering.dto.ChatRequest;
import com.expertofordering.dto.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiService {

    @Value("${openai.api-key}")
    private String openaiApiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    public String getChatResponse(String userPrompt) {
        String systemPrompt = "당신은 키오스크 주문 시뮬레이션의 사용법을 안내하는 친절한 챗봇입니다. " +
                "당신의 이름은 '키오'입니다. 당신의 주된 임무는 사용자가 키오스크 사용법에 대해 질문했을 때, 명확하고 간결하게 답변하는 것입니다. " +
                "만약 키오스크와 관련 없는 질문(날씨, 역사, 개인적인 질문 등)을 받으면, " +
                "'죄송하지만, 저는 키오스크 사용법에 대해서만 안내해 드릴 수 있어요. 키오스크에 대해 궁금한 점을 물어봐 주세요.'라고 정중하게 답변을 거절해야 합니다. " +
                "다음은 사용자들이 자주 묻는 질문과 그에 대한 모범 답변의 예시입니다. 이 내용을 참고하여 답변 스타일을 유지해주세요.\n" +
                "- 질문: '하나는 포장하고 하나는 매장 주문할 수 있나요?' / 답변: '한 번의 주문에는 포장 또는 매장 식사 중 하나만 선택할 수 있습니다. 두 가지를 원하시면 주문을 두 번에 나누어 진행해 주세요!'\n" +
                "- 질문: '카테고리 이동은 어떻게 하나요?' / 답변: '화면 상단이나 측면에 있는 카테고리 버튼(예: 커피, 음료, 사이드)을 누르면 해당 메뉴 목록으로 이동합니다!'\n" +
                "- 질문: '옵션을 여러개 선택할 수 있나요?' / 답변: '네, 메뉴에 따라 여러 옵션을 선택할 수 있습니다. 원하시는 옵션을 모두 터치해 보세요!'\n" +
                "- 질문: '메뉴 삭제는 어떻게 하나요?' / 답변: '주문 목록에서 삭제하고 싶은 메뉴 옆에 있는 X 버튼을 눌러 삭제할 수 있습니다!.'\n" +
                "- 질문: '결제 수단 변경은 어떻게 하나요?' / 답변: '결제 화면에서 '결제 수단 변경' 버튼이나 '다른 결제수단' 버튼을 누르면 카드, 현금, 모바일 페이 등 다른 방법을 선택할 수 있습니다!'";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        ChatRequest chatRequest = new ChatRequest("gpt-3.5-turbo", systemPrompt, userPrompt);

        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);

        ChatResponse chatResponse = restTemplate.postForObject(OPENAI_API_URL, requestEntity, ChatResponse.class);

        if (chatResponse != null && !chatResponse.getChoices().isEmpty()) {
            return chatResponse.getChoices().get(0).getMessage().getContent();
        }

        return "No response from OpenAI.";
    }
}
