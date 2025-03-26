package io.github.marcelohabreu.tripCollab.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.Role;
import io.github.marcelohabreu.tripCollab.entities.User;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDetailsResponse(UUID userId, String username, String email, Set<Role> roles,
                                  Set<PostAdminView> posts,
                                  @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                  LocalDateTime createdAt

) {

    public static UserDetailsResponse fromModel(User u) {
        Set<PostAdminView> posts = u.getPosts().stream().map(PostAdminView::fromModel).collect(Collectors.toSet());
        return new UserDetailsResponse(u.getUserId(), u.getUsername(), u.getEmail(), u.getRoles(), posts, u.getCreatedAt());
    }
}