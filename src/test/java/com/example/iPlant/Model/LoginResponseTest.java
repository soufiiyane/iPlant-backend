package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginResponseTest {

    @Test
    public void testGettersAndSetters() {
        LoginResponse response = new LoginResponse();
        response.setStatusCode(200);
        response.setBody("Success");

        assertEquals(200, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }
}