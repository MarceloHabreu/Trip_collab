package io.github.marcelohabreu.tripCollab.dtos.user;

import io.github.marcelohabreu.tripCollab.entities.User;

import java.util.UUID;

public record OtherUserResponse(UUID userId, String username, int followerCount, int followingCount, int postCount) {
    public static OtherUserResponse fromModel(User u){
        int postCount = u.getPosts() != null ? u.getPosts().size() : 0;
        return new OtherUserResponse(u.getUserId(), u.getUsername(), u.getFollowerCount(), u.getFollowingCount(), postCount);
    }
}
