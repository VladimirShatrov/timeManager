package vova.time_manager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.dto.UserPayload;
import vova.time_manager.model.User;
import vova.time_manager.service.UserService;

import java.net.URI;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController controller;

    @Test
    void createUser_PayloadValid_ReturnsValidResponseEntity() {
        String username = "user";
        String email = "user@mail.com";
        String password = "password";
        User newUser = new User(username, email, password);
        newUser.setId(1L);

        Mockito.when(userService.create(Mockito.any(User.class))).thenReturn(newUser);

        var responseEntity = controller.createUser(new UserPayload(username, email, password),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), Locale.ENGLISH);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if (responseEntity.getBody() instanceof User user) {
            assertNotNull(user.getId());
            assertEquals(username, user.getUsername());
            assertEquals(email, user.getEmail());
            assertEquals(password, user.getPassword());

            assertEquals(URI.create("http://localhost:8080/user/" + user.getId()),
                    responseEntity.getHeaders().getLocation());
        }
        else {
            assertInstanceOf(User.class, responseEntity.getBody());
        }

        verifyNoMoreInteractions(userService);
    }

}
