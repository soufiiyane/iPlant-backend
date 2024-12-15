package com.example.iPlant;

import com.example.iPlant.Model.ChatRequest;
import com.example.iPlant.Model.ChatMessageResponse;
import com.example.iPlant.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatRequest chatRequest) {
        try {
            // Validate input
            if (chatRequest.getInputText() == null || chatRequest.getInputText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Message cannot be empty");
            }

            if (chatRequest.getSession_id() == null || chatRequest.getSession_id().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Session ID is required");
            }

            ChatMessageResponse response = chatService.processChat(chatRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Error processing message: " + e.getMessage());
        }
    }
}
