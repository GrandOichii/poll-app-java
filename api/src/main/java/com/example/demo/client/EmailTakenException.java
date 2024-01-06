package com.example.demo.client;

import javax.naming.AuthenticationException;

public class EmailTakenException extends AuthenticationException {
    public EmailTakenException(final String email) {
        super("email " + email + " is taken");
    }
}
