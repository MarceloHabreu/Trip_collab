package io.github.marcelohabreu.tripCollab.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.dtos.post.SimplePostResponse;
import io.github.marcelohabreu.tripCollab.entities.Role;
import io.github.marcelohabreu.tripCollab.entities.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record AdminUserResponse(UUID userId, String username, String email, Set<Role> roles,
                                Set<SimplePostResponse> posts,
                                @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                LocalDateTime createdAt
) {

    public static AdminUserResponse fromModel(User u) {
        Set<SimplePostResponse> posts = u.getPosts().stream().map(SimplePostResponse::fromModel).collect(Collectors.toSet());
        return new AdminUserResponse(u.getUserId(), u.getUsername(), u.getEmail(), u.getRoles(), posts, u.getCreatedAt());
    }
}