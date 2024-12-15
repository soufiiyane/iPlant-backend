package com.example.iPlant.controller;

import com.example.iPlant.CommentController;
import com.example.iPlant.Model.Comment;
import com.example.iPlant.Service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Comment validComment;

    @BeforeEach
    void setUp() {
        validComment = new Comment();
        validComment.setPlantId("plant123");
        validComment.setUserId("user123");
        validComment.setText("This is a test comment");
        validComment.setFirstName("John");
        validComment.setLastName("Doe");
        validComment.setUserImageUrl("http://example.com/image.jpg");
    }

    @Test
    public void whenValidInput_thenReturns200() throws Exception {
        when(commentService.addComment(any(Comment.class)))
                .thenReturn(ResponseEntity.ok("Comment added successfully"));

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Comment added successfully"));
    }

    @Test
    public void whenNullPlantId_thenReturns400() throws Exception {
        Comment commentWithNullPlantId = new Comment();
        commentWithNullPlantId.setUserId("user123");
        commentWithNullPlantId.setText("Test comment");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentWithNullPlantId)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Plant ID is required"));
    }

    @Test
    public void whenEmptyPlantId_thenReturns400() throws Exception {
        validComment.setPlantId("");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Plant ID is required"));
    }

    @Test
    public void whenNullText_thenReturns400() throws Exception {
        validComment.setText(null);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Comment text is required"));
    }

    @Test
    public void whenEmptyText_thenReturns400() throws Exception {
        validComment.setText("   ");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Comment text is required"));
    }

    @Test
    public void whenNullUserId_thenReturns400() throws Exception {
        validComment.setUserId(null);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User ID is required"));
    }

    @Test
    public void whenEmptyUserId_thenReturns400() throws Exception {
        validComment.setUserId("  ");

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User ID is required"));
    }

    @Test
    public void whenServiceThrowsException_thenReturns500() throws Exception {
        when(commentService.addComment(any(Comment.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validComment)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error adding comment: Service error"));
    }

    @Test
    public void whenInvalidJson_thenReturns400() throws Exception {
        String invalidJson = "{\"plantId\":\"123\", \"text\":\"test\", ";  // Invalid JSON

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenExtraFields_thenIgnoresAndProcessesSuccessfully() throws Exception {
        String jsonWithExtraFields = objectMapper.writeValueAsString(validComment)
                .replace("}", ", \"extraField\": \"value\"}");

        when(commentService.addComment(any(Comment.class)))
                .thenReturn(ResponseEntity.ok("Comment added successfully"));

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithExtraFields))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Comment added successfully"));
    }

}
