package com.example.iPlant.Model;

public class LoginRequest {
    private LoginBody body;

    public static class LoginBody {
        private String email;
        private String password;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Getters and Setters
    public LoginBody getBody() {
        return body;
    }

    public void setBody(LoginBody body) {
        this.body = body;
    }
}
