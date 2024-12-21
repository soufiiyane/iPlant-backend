package com.example.iPlant.Service;

import com.example.iPlant.Model.ChatRequest;
import com.example.iPlant.Model.ChatResponse;
import com.example.iPlant.Model.ChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ChatServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessChat() throws Exception {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setInputText("Hello");
        chatRequest.setSession_id("session123");

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setStatusCode(200);
        chatResponse.setBody("{\"messages\":[\"Hello\",\"World\"]}");

        ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
        chatMessageResponse.setMessages(Arrays.asList("Hello", "World"));

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>("{\"statusCode\":200,\"body\":\"{\\\"messages\\\":[\\\"Hello\\\",\\\"World\\\"]}\"}", HttpStatus.OK));
        when(objectMapper.readValue(any(String.class), any(Class.class)))
                .thenReturn(chatResponse)
                .thenReturn(chatMessageResponse);

        ChatMessageResponse result = chatService.processChat(chatRequest);

        assertEquals(chatMessageResponse.getMessages(), result.getMessages());
    }
}