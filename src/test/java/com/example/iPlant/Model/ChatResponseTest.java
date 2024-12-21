package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatResponseTest {

    @Test
    public void testGettersAndSetters() {
        ChatResponse response = new ChatResponse();
        response.setStatusCode(200);
        response.setBody("Success");

        assertEquals(200, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }
}