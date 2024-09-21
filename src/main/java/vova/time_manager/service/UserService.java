package vova.time_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public User findUserByLogin (String login) {
        return userRepository.findByLogin(login).orElseThrow(() ->
                new NoSuchElementException("User with login: " + login + " not found."));
    }

    public User findUserById (Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("User with id: " + id + " not found."));
    }
}
