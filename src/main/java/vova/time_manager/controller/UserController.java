package vova.time_manager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import vova.time_manager.dto.UserPayload;
import vova.time_manager.model.User;
import vova.time_manager.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
            @RequestBody UserPayload createUserPayload,
            UriComponentsBuilder uriComponentsBuilder) {
        User user = new User(createUserPayload.username(),
                 createUserPayload.email(),
                 createUserPayload.password());
        User newUser = userService.create(user);
        return ResponseEntity.created(uriComponentsBuilder
                .path("/{id}")
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
