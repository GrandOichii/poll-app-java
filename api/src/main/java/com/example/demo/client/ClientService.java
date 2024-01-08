package com.example.demo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client register(LoginClient register) throws EmailTakenException, InvalidRegisterCredentialsException {
        var existing = clientRepository.getClientByEmail(register.getEmail());
        if (existing.isPresent()) throw new EmailTakenException(register.getEmail());

//        TODO hash email
        var passHash = register.getPassword();
        var result = new Client(register.getEmail(), passHash);
        result.checkValid();
        clientRepository.save(result);
        return result;
    }

    public String login(LoginClient login) throws AuthenticationFailedException {
        var existing = clientRepository.getClientByEmail(login.getEmail());
        if (existing.isEmpty()) throw new AuthenticationFailedException();

//        TODO check actual hash
        if (!login.getPassword().equals(existing.get().getPasswordHash())) throw new AuthenticationFailedException();

//        TODO return actual JWT token
        return "jwt token test";
    }

}
