package com.example.iPlant;

import com.example.iPlant.Model.LoginRequest;
import com.example.iPlant.Model.LoginResponse;
import com.example.iPlant.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validate input
            if (loginRequest.getBody() == null ||
                    StringUtils.isEmpty(loginRequest.getBody().getEmail()) ||
                    StringUtils.isEmpty(loginRequest.getBody().getPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body("Please enter your email and password");
            }

            LoginResponse response = loginService.login(loginRequest);

            if (response.getStatusCode() == 200) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid login credentials");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}