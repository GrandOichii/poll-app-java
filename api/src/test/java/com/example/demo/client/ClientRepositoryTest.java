package com.example.demo.client;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void shouldSave() {
//        arrange
        var client = new Client("mymail@email.com", "pass-hash");
//        act
        clientRepository.save(client);
        var result = clientRepository.findAll();

//        assert
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void shouldFetchByEmail() {
        var email = "mymail@email.com";
//        arrange
        var client = new Client(email, "pass-hash");
//        act
        clientRepository.save(client);
        var result = clientRepository.getClientByEmail(email);

//        assert
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getEmail()).isEqualTo(client.getEmail());
        Assertions.assertThat(result.get().getPasswordHash()).isEqualTo(client.getPasswordHash());
    }

    @Test
    public void shouldNotFetchByEmail() {
//        arrange
        var client = new Client("mymail@email.com", "pass-hash");
//        act
        clientRepository.save(client);
        var result = clientRepository.getClientByEmail("nonexistant@email.com");

//        assert
        Assertions.assertThat(result.isEmpty()).isTrue();
    }
}
