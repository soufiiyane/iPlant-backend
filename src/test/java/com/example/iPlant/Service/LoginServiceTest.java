package com.example.iPlant.Service;

import com.example.iPlant.Model.LoginRequest;
import com.example.iPlant.Model.LoginResponse;
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

public class LoginServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest();
        LoginRequest.LoginBody loginBody = new LoginRequest.LoginBody();
        loginBody.setEmail("test@example.com");
        loginBody.setPassword("password123");
        loginRequest.setBody(loginBody);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatusCode(200);
        loginResponse.setBody("Success");

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(loginResponse, HttpStatus.OK));

        LoginResponse response = loginService.login(loginRequest);

        assertEquals(200, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }
}