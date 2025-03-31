package io.github.marcelohabreu.tripCollab.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        String username,
        @Email(message = "Email must be a valid email address")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password) {
}