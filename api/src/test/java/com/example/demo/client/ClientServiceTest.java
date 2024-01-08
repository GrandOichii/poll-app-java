package com.example.demo.client;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Test
    public void shouldRegister() throws Exception {
//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        var client = new Client(login.getEmail(), login.getPassword());
        Mockito.when(clientRepository.getClientByEmail(client.getEmail())).thenReturn(Optional.empty());
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

//        act
        var result = clientService.register(login);

//        assert
        Assertions.assertThat(result.getEmail()).isEqualTo(client.getEmail());

    }

//    @ParameterizedTest
//    @ValueSource()
    @Test
    public void shouldNotRegister() {

//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        var client = new Client(login.getEmail(), login.getPassword());
        Mockito.when(clientRepository.getClientByEmail(client.getEmail())).thenReturn(Optional.of(client));

//        act
        ThrowableAssert.ThrowingCallable action = () -> {
            clientService.register(login);
        };

//        assert
        Assertions.assertThatThrownBy(action).isInstanceOf(EmailTakenException.class);
    }

    @ParameterizedTest
    @CsvSource({"myemail@mail.com,", ",password"})
    public void failedRegister(String email, String password) {
        if (email == null) email = "";
        if (password == null) password = "";

//        arrange
        var login = new LoginClient(email, password);
        var client = new Client(login.getEmail(), login.getPassword());
        Mockito.when(clientRepository.getClientByEmail(client.getEmail())).thenReturn(Optional.empty());

//        act
        ThrowableAssert.ThrowingCallable action = () -> {
            clientService.register(login);
        };

//        assert
        Assertions.assertThatThrownBy(action).isInstanceOf(InvalidRegisterCredentialsException.class);
    }

    @Test
    public void shouldLogin() throws Exception {
//        arrange
        var login = createUser();

//        act
        var result = clientService.login(login);

//        assert
        Assertions.assertThat(result.length()).isGreaterThan(0);
    }

    @Test
    public void shouldNotLogin() throws Exception {
//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        var client = new Client(login.getEmail(), login.getPassword());
        Mockito.when(clientRepository.getClientByEmail(client.getEmail())).thenReturn(Optional.empty());

//        act
        ThrowableAssert.ThrowingCallable action = () -> {
            clientService.login(login);
        };

//        assert
        Assertions.assertThatThrownBy(action).isInstanceOf(AuthenticationFailedException.class);
    }


    LoginClient createUser() throws Exception {
        var result = new LoginClient("mymail@email.com", "password");
        var client = new Client(result.getEmail(), result.getPassword());
        Mockito.when(clientRepository.getClientByEmail(client.getEmail())).thenReturn(Optional.empty()).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

//        act
        clientService.register(result);

        return result;
    }
}
