package com.example.iPlant.controller;

import com.example.iPlant.LoginController;
import com.example.iPlant.Model.LoginRequest;
import com.example.iPlant.Model.LoginResponse;
import com.example.iPlant.Service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest validLoginRequest;
    private LoginRequest invalidLoginRequest;
    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    @BeforeEach
    void setUp() {
        // Set up valid login request
        validLoginRequest = new LoginRequest();
        LoginRequest.LoginBody validBody = new LoginRequest.LoginBody();
        validBody.setEmail("imad@gmail.com");
        validBody.setPassword("imad");
        validLoginRequest.setBody(validBody);

        invalidLoginRequest = new LoginRequest();
        LoginRequest.LoginBody invalidBody = new LoginRequest.LoginBody();
        invalidBody.setEmail("");
        invalidBody.setPassword("");
        invalidLoginRequest.setBody(invalidBody);

        // Set up success response
        successResponse = new LoginResponse();
        successResponse.setStatusCode(200);
        successResponse.setBody("Login successful");

        // Set up failure response
        failureResponse = new LoginResponse();
        failureResponse.setStatusCode(401);
        failureResponse.setBody("Invalid credentials");
    }

    @Test
    public void whenValidInput_thenReturns200() throws Exception {
        when(loginService.login(any(LoginRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.body").value("Login successful"));
    }

    @Test
    public void whenInvalidCredentials_thenReturns401() throws Exception {
        when(loginService.login(any(LoginRequest.class))).thenReturn(failureResponse);

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid login credentials"));
    }

    @Test
    public void whenEmptyCredentials_thenReturns400() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please enter your email and password"));
    }

    @Test
    public void whenNullBody_thenReturns400() throws Exception {
        LoginRequest requestWithNullBody = new LoginRequest();

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNullBody)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please enter your email and password"));
    }

    @Test
    public void whenServiceThrowsException_thenReturns500() throws Exception {
        when(loginService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: Something went wrong"));
    }
}