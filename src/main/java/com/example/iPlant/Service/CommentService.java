package com.example.iPlant.Service;
import com.example.iPlant.Model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommentService {
    private final RestTemplate restTemplate;
    private final String COMMENT_API_URL = "https://ssmb5oqxxa.execute-api.us-east-1.amazonaws.com/dev/comment";

    @Autowired
    public CommentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> addComment(Comment comment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Comment> request = new HttpEntity<>(comment, headers);

        return restTemplate.postForEntity(
                COMMENT_API_URL,
                request,
                String.class
        );
    }
}