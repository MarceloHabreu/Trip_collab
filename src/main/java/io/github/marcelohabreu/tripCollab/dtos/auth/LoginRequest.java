package io.github.marcelohabreu.tripCollab.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "The e-mail field is required")
        @Email(message = "Invalid e-mail format")
        String email,

        @NotBlank(message = "The password field is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password) {
}
