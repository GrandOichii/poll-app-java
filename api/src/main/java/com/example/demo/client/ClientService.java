package com.example.demo.client;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Client register(LoginClient register) throws EmailTakenException, InvalidRegisterCredentialsException {
        var existing = clientRepository.getClientByEmail(register.getEmail());
        if (existing.isPresent()) throw new EmailTakenException(register.getEmail());
        register.checkValid();
        var passHash = passwordEncoder.encode(register.getPassword());
//        var passHash = register.getPassword();
        var result = new Client(register.getEmail(), passHash);
        System.out.println(result.toString());
        clientRepository.save(result);
        return result;
    }

    public String login(LoginClient login) throws AuthenticationFailedException {
        var existing = clientRepository.getClientByEmail(login.getEmail());
        if (existing.isEmpty()) throw new AuthenticationFailedException();

        if (!passwordEncoder.matches(login.getPassword(), existing.get().getPasswordHash())) throw new AuthenticationFailedException();

//        TODO return actual JWT token
        return "jwt token test";
    }

}
