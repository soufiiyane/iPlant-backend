package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatMessageResponseTest {

    @Test
    public void testGetMessages() {
        ChatMessageResponse response = new ChatMessageResponse();
        List<String> messages = Arrays.asList("Hello", "World");
        response.setMessages(messages);
        assertEquals(messages, response.getMessages());
    }

    @Test
    public void testSetMessages() {
        ChatMessageResponse response = new ChatMessageResponse();
        List<String> messages = Arrays.asList("Test", "Message");
        response.setMessages(messages);
        assertEquals(messages, response.getMessages());
    }
}