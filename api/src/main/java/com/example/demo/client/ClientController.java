package com.example.demo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<GetClient> register(@RequestBody LoginClient client) {
        try {
            var result = clientService.register(client);
            return new ResponseEntity<>(result.toGetClient(), HttpStatus.CREATED);
        } catch (EmailTakenException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (InvalidRegisterCredentialsException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginClient client) {
        try {
            var result = clientService.login(client);
            return ResponseEntity.ok(result);
        } catch (AuthenticationFailedException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
