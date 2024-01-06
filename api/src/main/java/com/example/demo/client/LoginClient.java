package com.example.demo.client;

public class LoginClient {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LoginClient(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
