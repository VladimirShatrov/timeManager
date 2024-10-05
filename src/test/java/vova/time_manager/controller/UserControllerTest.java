package vova.time_manager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.dto.UserPayload;
import vova.time_manager.error.ErrorsPresentation;
import vova.time_manager.model.User;
import vova.time_manager.service.UserService;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    MessageSource messageSource;

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

    @Test
    void createUser_PayloadUsernameIsInvalid_ReturnsValidResponseEntity() {
        String username = "";
        String email = "user@mail.com";
        String password = "password";
        Locale locale = Locale.ENGLISH;

        String errorMessage = "Username is empty";
        doReturn(errorMessage).when(messageSource)
                        .getMessage("user.create.username.errors.not_set", new Object[0], locale);

        var responseEntity = controller.createUser(new UserPayload(username, email, password),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), locale);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        verifyNoInteractions(userService);
    }

    @Test
    void createUser_PayloadEmailIsInvalid_ReturnsValidResponseEntity() {
        String username = "user";
        String email = "   ";
        String password = "password";
        Locale locale = Locale.ENGLISH;

        String errorMessage = "Email is empty";
        doReturn(errorMessage).when(messageSource)
                .getMessage("user.create.email.errors.not_set", new Object[0], locale);


        var responseEntity = controller.createUser(new UserPayload(username, email, password),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), locale);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        verifyNoInteractions(userService);
    }

    @Test
    void createUser_PayloadPasswordIsInvalid_ReturnsValidResponseEntity() {
        String username = "user";
        String email = "@mail.com";
        String password = "";
        Locale locale = Locale.ENGLISH;

        String errorMessage = "Password is empty";
        doReturn(errorMessage).when(messageSource)
                .getMessage("user.create.password.errors.not_set", new Object[0], locale);


        var responseEntity = controller.createUser(new UserPayload(username, email, password),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), locale);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        verifyNoInteractions(userService);
    }

    @Test
    void findById_ReturnsValidResponseEntity() {
        String username = "user";
        String email = "user@mail.com";
        String password = "password";
        User user = new User(username, email, password);
        user.setId(1L);

        Mockito.when(userService.findUserById(1L)).thenReturn(user);

        var responseEntity = controller.findById(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody(), user);
    }



}
