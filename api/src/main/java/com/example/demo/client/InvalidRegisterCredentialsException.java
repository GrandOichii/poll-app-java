package com.example.demo.client;

public class InvalidRegisterCredentialsException extends Exception{
    public InvalidRegisterCredentialsException() {
        super("invalid register credentials");
    }

}
