package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDetailsTest {

    @Test
    public void testGettersAndSetters() {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName("John");
        userDetails.setLastName("Doe");
        userDetails.setImageUrl("http://example.com/image.jpg");

        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertEquals("http://example.com/image.jpg", userDetails.getImageUrl());
    }
}