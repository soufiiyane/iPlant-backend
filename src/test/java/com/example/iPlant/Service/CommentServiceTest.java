package com.example.iPlant.Service;

import com.example.iPlant.Model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CommentServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment();
        comment.setPlantId("123");
        comment.setFirstName("John");
        comment.setLastName("Doe");
        comment.setText("This is a comment.");
        comment.setUserId("user123");
        comment.setUserImageUrl("http://example.com/image.jpg");

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        ResponseEntity<String> response = commentService.addComment(comment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }
}