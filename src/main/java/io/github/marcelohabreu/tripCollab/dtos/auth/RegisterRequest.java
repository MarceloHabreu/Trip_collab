package io.github.marcelohabreu.tripCollab.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "The username field is required")
        @Size(min = 3, max = 50, message = "The username must be between 3 and 50 characters long")
        String username,

        @NotBlank(message = "The e-mail field is required")
        @Email(message = "Invalid e-mail format")
        String email,

        @NotBlank(message = "The password field is required")
        @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
        String password) {
}
