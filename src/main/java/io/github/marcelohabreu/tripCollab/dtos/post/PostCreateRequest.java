package io.github.marcelohabreu.tripCollab.dtos.post;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank(message = "The title field is required")
        @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
        String title,
        @NotBlank(message = "The body field is required")
        @Size(min = 5, message = "Body must have at least 5 characters")
        String body,
        @NotBlank(message = "The location field is required")
        @Size(min = 5, message = "Body must have at least 5 characters")
        String location) {
}
