package com.example.iPlant;

import com.example.iPlant.Model.Comment;
import com.example.iPlant.Service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        logger.info("Received comment: {}", comment);
        try {
            if (comment.getPlantId() == null || comment.getPlantId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Plant ID is required");
            }
            if (comment.getText() == null || comment.getText().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Comment text is required");
            }
            if (comment.getUserId() == null || comment.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }

            ResponseEntity<String> response = commentService.addComment(comment);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            logger.error("Error adding comment", e);
            return ResponseEntity.internalServerError()
                    .body("Error adding comment: " + e.getMessage());
        }
    }
}