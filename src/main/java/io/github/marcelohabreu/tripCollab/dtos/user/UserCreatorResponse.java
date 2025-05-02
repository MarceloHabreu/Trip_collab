package io.github.marcelohabreu.tripCollab.dtos.user;

import io.github.marcelohabreu.tripCollab.entities.User;

import java.util.UUID;

public record UserCreatorResponse(UUID userId, String username, int followerCount, int postCount) {
    public static UserCreatorResponse fromModel(User u) {
        int postCount = u.getPosts() != null ? u.getPosts().size() : 0;
        return new UserCreatorResponse(u.getUserId(), u.getUsername(), u.getFollowerCount(), postCount);
    }
}
