package vova.time_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vova.time_manager.dto.UserPayload;
import vova.time_manager.model.User;
import vova.time_manager.repository.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByEmail (String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NoSuchElementException("User with email: " + email + " not found."));
    }

    public User findUserByUsername (String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NoSuchElementException("User with login: " + username + " not found."));
    }

    public User findUserById (Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("User with id: " + id + " not found."));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User with this username already exists.");
        }
        else if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with this email already exists.");
        }

        return save(user);
    }

    public UserDetailsService userDetailsService() {
        return this::findUserByUsername;
    }

    public User editUser(Long id, UserPayload userPayload) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id: " + id + " not found."));

        if (userPayload.email() != null) {
            if (userRepository.existsByEmail(userPayload.email())) {
                throw new RuntimeException("User with email: " + userPayload.email() + " already exists.");
            }
            user.setEmail(userPayload.email());
        }

        if (userPayload.username() != null) {
            if (userRepository.existsByUsername(userPayload.username())) {
                throw new RuntimeException("User with username: " + userPayload.username() + " already exists.");
            }
            user.setUsername(userPayload.username());
        }

        if (userPayload.password() != null) {
            user.setPassword(userPayload.password());
        }

        return save(user);
    }
}
