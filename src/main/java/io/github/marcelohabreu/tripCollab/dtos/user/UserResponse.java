package io.github.marcelohabreu.tripCollab.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String username, String email, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                               LocalDateTime createdAt){
    public static UserResponse fromModel(User u){
        return new UserResponse(u.getUserId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
}
