package io.github.marcelohabreu.tripCollab.dtos.auth;

public record AuthResponse(String message,
                           String token,
                           Long expiresIn) {
}
