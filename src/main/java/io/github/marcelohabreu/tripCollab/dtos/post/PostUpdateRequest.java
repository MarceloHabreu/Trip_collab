package io.github.marcelohabreu.tripCollab.dtos.post;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record PostUpdateRequest(
        @Size(min = 5, max = 50, message = "Title must be between 5 and 50 characters")
        String title,
        @Size(min = 5, message = "Body must have at least 5 characters")
        String body,
        @Size(min = 5, message = "Location must have at least 5 characters")
        String location,
        List<UUID> imagesToRemove
) {

}
