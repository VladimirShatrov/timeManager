package vova.time_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.dto.UserPayload;
import vova.time_manager.error.ErrorsPresentation;
import vova.time_manager.model.User;
import vova.time_manager.service.UserService;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final MessageSource messageSource;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @RequestBody UserPayload createUserPayload,
            UriComponentsBuilder uriComponentsBuilder,
            Locale locale) {
        if (createUserPayload.username() == null || createUserPayload.username().isBlank()) {
            final var messageUsername = messageSource
                    .getMessage("user.create.username.errors.not_set",
                            new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(List.of(messageUsername)));
        }
        if (createUserPayload.email() == null || createUserPayload.email().isBlank()) {
            final var messageEmail = messageSource
                    .getMessage("user.create.email.errors.not_set",
                            new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(List.of(messageEmail)));
        }
        if (createUserPayload.password() == null || createUserPayload.password().isBlank()) {
            final var messagePassword = messageSource
                    .getMessage("user.create.password.errors.not_set",
                            new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsPresentation(List.of(messagePassword)));
        }
        User user = new User(createUserPayload.username(),
                 createUserPayload.email(),
                 createUserPayload.password());
        User newUser = userService.create(user);
        return ResponseEntity.created(uriComponentsBuilder
                .path("user/{id}")
                .build(Map.of("id", newUser.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findUserById(id));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<User> editUser(
            @RequestBody UserPayload userPayload,
            @PathVariable Long id) {
        User editUser = userService.editUser(id, userPayload);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(editUser);
    }
}
