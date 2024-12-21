package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginRequestTest {

    @Test
    public void testGettersAndSetters() {
        LoginRequest.LoginBody body = new LoginRequest.LoginBody();
        body.setEmail("test@example.com");
        body.setPassword("password123");

        assertEquals("test@example.com", body.getEmail());
        assertEquals("password123", body.getPassword());

        LoginRequest request = new LoginRequest();
        request.setBody(body);

        assertEquals(body, request.getBody());
    }
}