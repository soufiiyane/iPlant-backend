package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatRequestTest {

    @Test
    public void testGettersAndSetters() {
        ChatRequest request = new ChatRequest();
        request.setInputText("Hello");
        request.setSession_id("session123");

        assertEquals("Hello", request.getInputText());
        assertEquals("session123", request.getSession_id());
    }
}