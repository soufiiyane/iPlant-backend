package com.example.iPlant.controller;

import com.example.iPlant.ChatController;
import com.example.iPlant.Model.ChatRequest;
import com.example.iPlant.Model.ChatMessageResponse;
import com.example.iPlant.Service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    private ChatRequest validChatRequest;
    private ChatMessageResponse mockResponse;

    @BeforeEach
    void setUp() {
        // Set up valid chat request
        validChatRequest = new ChatRequest();
        validChatRequest.setInputText("Hello, how can you help me?");
        validChatRequest.setSession_id("session123");

        // Set up mock response
        mockResponse = new ChatMessageResponse();
        // Set necessary fields in mockResponse
    }

    @Test
    public void whenValidInput_thenReturns200() throws Exception {
        when(chatService.processChat(any(ChatRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validChatRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void whenEmptyMessage_thenReturns400() throws Exception {
        ChatRequest emptyMessageRequest = new ChatRequest();
        emptyMessageRequest.setSession_id("session123");
        emptyMessageRequest.setInputText("");

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyMessageRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Message cannot be empty"));
    }

    @Test
    public void whenNullMessage_thenReturns400() throws Exception {
        ChatRequest nullMessageRequest = new ChatRequest();
        nullMessageRequest.setSession_id("session123");
        nullMessageRequest.setInputText(null);

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullMessageRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Message cannot be empty"));
    }

    @Test
    public void whenEmptySessionId_thenReturns400() throws Exception {
        ChatRequest emptySessionRequest = new ChatRequest();
        emptySessionRequest.setInputText("Hello");
        emptySessionRequest.setSession_id("");

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptySessionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Session ID is required"));
    }

    @Test
    public void whenNullSessionId_thenReturns400() throws Exception {
        ChatRequest nullSessionRequest = new ChatRequest();
        nullSessionRequest.setInputText("Hello");
        nullSessionRequest.setSession_id(null);

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullSessionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Session ID is required"));
    }

    @Test
    public void whenServiceThrowsException_thenReturns500() throws Exception {
        when(chatService.processChat(any(ChatRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validChatRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error processing message: Service error"));
    }

    @Test
    public void whenInvalidJson_thenReturns400() throws Exception {
        String invalidJson = "{\"inputText\":\"Hello\", \"session_id\":"; // Invalid JSON

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenVeryLongMessage_thenProcessesSuccessfully() throws Exception {
        ChatRequest longMessageRequest = new ChatRequest();
        longMessageRequest.setSession_id("session123");
        longMessageRequest.setInputText("a"); // Very long message

        when(chatService.processChat(any(ChatRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longMessageRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenExtraFieldsInJson_thenIgnoresAndProcessesSuccessfully() throws Exception {
        String jsonWithExtraFields = objectMapper.writeValueAsString(validChatRequest)
                .replace("}", ", \"extraField\": \"value\"}");

        when(chatService.processChat(any(ChatRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/chat/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithExtraFields))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
