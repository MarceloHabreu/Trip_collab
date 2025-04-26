package io.github.marcelohabreu.tripCollab.dtos.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResponse(UUID id, String username, String email, int followerCount, int followingCount, int postCount, @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                               LocalDateTime createdAt){
    public static UserProfileResponse fromModel(User u){
        int postCount = u.getPosts() != null ? u.getPosts().size() : 0;
        return new UserProfileResponse(u.getUserId(), u.getUsername(), u.getEmail(), u.getFollowerCount(), u.getFollowingCount(), postCount, u.getCreatedAt());
    }
}
