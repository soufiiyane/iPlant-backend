package com.example.iPlant.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentTest {
    @Test
    public void testGettersAndSetters() {
        Comment comment = new Comment();

        comment.setPlantId("123");
        assertEquals("123", comment.getPlantId());

        comment.setFirstName("John");
        assertEquals("John", comment.getFirstName());

        comment.setLastName("Doe");
        assertEquals("Doe", comment.getLastName());

        comment.setText("This is a comment.");
        assertEquals("This is a comment.", comment.getText());

        comment.setUserId("user123");
        assertEquals("user123", comment.getUserId());

        comment.setUserImageUrl("http://example.com/image.jpg");
        assertEquals("http://example.com/image.jpg", comment.getUserImageUrl());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment();
        comment.setPlantId("123");
        comment.setFirstName("John");
        comment.setLastName("Doe");
        comment.setText("This is a comment.");
        comment.setUserId("user123");
        comment.setUserImageUrl("http://example.com/image.jpg");

        String expected = "Comment{plantId='123', firstName='John', lastName='Doe', text='This is a comment.', userId='user123', userImageUrl='http://example.com/image.jpg'}";
        assertEquals(expected, comment.toString());
    }
}
