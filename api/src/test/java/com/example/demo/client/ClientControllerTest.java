package com.example.demo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ClientService clientService;

    @Test
    public void shouldRegister() throws Exception {
//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        Mockito.when(clientService.register(any(LoginClient.class))).thenReturn(new Client("mymail@email.com", "hashed-password"));

//        act
//        assert
        mvc
                .perform(post("/api/v1/auth/register")
                        .content(toJson(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldNotRegister() throws Exception {
//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        Mockito.when(clientService.register(any(LoginClient.class))).thenThrow(EmailTakenException.class);

//        act
//        assert
        mvc
                .perform(post("/api/v1/auth/register")
                        .content(toJson(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());

    }


    @Test
    public void shouldLogin() throws Exception {
//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        Mockito.when(clientService.login(any(LoginClient.class))).thenReturn("test jwt token");

//        act
//        assert
        mvc
                .perform(post("/api/v1/auth/login")
                        .content(toJson(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


    @Test
    public void shouldFailLogin() throws Exception {
//        arrange
        var login = new LoginClient("mymail@email.com", "password");
        Mockito.when(clientService.login(any(LoginClient.class))).thenThrow(AuthenticationFailedException.class);

//        act
//        assert
        mvc
                .perform(post("/api/v1/auth/login")
                        .content(toJson(login))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());


    }

    private static String toJson(final Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
