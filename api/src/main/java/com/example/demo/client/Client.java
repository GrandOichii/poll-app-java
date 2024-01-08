package com.example.demo.client;

import jakarta.persistence.*;

@Entity
@Table
public class Client {
    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )

    private long id;
    private String email;

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    private String passwordHash;

    public Client(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Client() {
    }

    public GetClient toGetClient() {
        return new GetClient(this.email);
    }

    public void checkValid() throws InvalidRegisterCredentialsException {
        var e = new InvalidRegisterCredentialsException();

        if (email == null || email.isEmpty()) throw e;
//        TODO change
        if (passwordHash == null || passwordHash.isEmpty()) throw e;

    }
}
