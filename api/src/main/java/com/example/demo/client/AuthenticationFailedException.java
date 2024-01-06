package com.example.demo.client;

import javax.naming.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {
    public AuthenticationFailedException() {
        super("failed to authenticate");
    }
}
