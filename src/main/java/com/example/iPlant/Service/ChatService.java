package com.example.iPlant.Service;

import com.example.iPlant.Model.ChatRequest;
import com.example.iPlant.Model.ChatResponse;
import com.example.iPlant.Model.ChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {
    private final RestTemplate restTemplate;
    private final String CHAT_API_URL = "https://ssmb5oqxxa.execute-api.us-east-1.amazonaws.com/dev/agent";
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ChatMessageResponse processChat(ChatRequest chatRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ChatRequest> request = new HttpEntity<>(chatRequest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    CHAT_API_URL,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse the initial response
                ChatResponse chatResponse = objectMapper.readValue(
                        response.getBody(),
                        ChatResponse.class
                );

                // Parse the body string to get the messages
                ChatMessageResponse messageResponse = objectMapper.readValue(
                        chatResponse.getBody(),
                        ChatMessageResponse.class
                );

                return messageResponse;
            } else {
                throw new RuntimeException("Failed to process chat: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing chat: " + e.getMessage());
        }
    }
}
