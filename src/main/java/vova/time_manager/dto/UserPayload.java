package vova.time_manager.dto;

public record UserPayload(
        String username,
        String email,
        String password
) {
}
