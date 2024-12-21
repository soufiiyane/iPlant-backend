package com.example.iPlant.controller;

import com.example.iPlant.LoginController;
import com.example.iPlant.Model.LoginRequest;
import com.example.iPlant.Model.LoginResponse;
import com.example.iPlant.Service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        LoginRequest.LoginBody loginBody = new LoginRequest.LoginBody();
        loginBody.setEmail("test@example.com");
        loginBody.setPassword("password123");
        loginRequest.setBody(loginBody);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatusCode(200);
        loginResponse.setBody("Success");

        when(loginService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginResponse, response.getBody());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        LoginRequest.LoginBody loginBody = new LoginRequest.LoginBody();
        loginBody.setEmail("test@example.com");
        loginBody.setPassword("wrongpassword");
        loginRequest.setBody(loginBody);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatusCode(401);
        loginResponse.setBody("Invalid login credentials");

        when(loginService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid login credentials", response.getBody());
    }

    @Test
    public void testLogin_MissingCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        LoginRequest.LoginBody loginBody = new LoginRequest.LoginBody();
        loginBody.setEmail("");
        loginBody.setPassword("");
        loginRequest.setBody(loginBody);

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Please enter your email and password", response.getBody());
    }

    @Test
    public void testLogin_InternalServerError() {
        LoginRequest loginRequest = new LoginRequest();
        LoginRequest.LoginBody loginBody = new LoginRequest.LoginBody();
        loginBody.setEmail("test@example.com");
        loginBody.setPassword("password123");
        loginRequest.setBody(loginBody);

        when(loginService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<?> response = loginController.login(loginRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error: Service error", response.getBody());
    }
}