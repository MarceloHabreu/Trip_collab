package io.github.marcelohabreu.tripCollab.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(min = 3, max = 50, message = "The username must be between 3 and 50 characters long")
        String username,
        @Email(message = "Invalid e-mail format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,
        @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
        String password) {
}
