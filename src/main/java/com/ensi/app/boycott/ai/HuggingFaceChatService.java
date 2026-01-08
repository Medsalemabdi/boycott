package com.ensi.app.boycott.ai;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceChatService {

    @Value("${huggingface.api.token}")
    private String apiToken;

    private static final String API_URL =
            "https://router.huggingface.co/v1/chat/completions";

    private static final String MODEL =
            "deepseek-ai/DeepSeek-V3.2:novita";

    private final ObjectMapper mapper = new ObjectMapper();

    public AiResult analyzeProduct(String productName) {

        RestTemplate restTemplate = new RestTemplate();

        String prompt = """
You are an API.
Answer ONLY in valid JSON.
Do NOT add explanations.

Given the product name: "%s"

Return exactly this JSON:
{
  "boycotted": boolean,
  "alternative": string | null,
  "reason": string | null
}

If the boycott reason is related to Palestine, clearly mention it.
If unsure, set all fields to null or false.
""".formatted(productName);


        Map<String, Object> body = Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                ),
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(API_URL, request, Map.class);

        return parseJsonResponse(productName, response.getBody());
    }

    // 🔹 JSON parsing SAFE
    private AiResult parseJsonResponse(String productName, Map body) {

        try {
            List choices = (List) body.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");

            String json = message.get("content").toString();

            Map<String, Object> parsed =
                    mapper.readValue(json, Map.class);

            boolean boycotted =
                    (boolean) parsed.getOrDefault("boycotted", false);

            String alternative =
                    (String) parsed.get("alternative");

            String reason =
                    (String) parsed.get("reason");

            return new AiResult(
                    productName,
                    boycotted,
                    alternative,
                    reason,
                    "AI - Hugging Face (JSON)",
                    json // ← raw LLM response
            );

        } catch (Exception e) {
            return new AiResult(
                    productName,
                    false,
                    null,
                    null,
                    "AI - Hugging Face (parse error)",
                    null
            );
        }
    }

}


