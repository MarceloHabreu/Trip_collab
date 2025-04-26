package io.github.marcelohabreu.tripCollab.dtos.user;

import io.github.marcelohabreu.tripCollab.entities.User;
import io.github.marcelohabreu.tripCollab.entities.compostiteKey.FollowerId;
import io.github.marcelohabreu.tripCollab.repositories.FollowerRepository;


import java.util.UUID;

public record SimpleUserResponse(UUID userId, String username, boolean isFollowing) {
    public static SimpleUserResponse fromModel(User user, User authenticatedUser, FollowerRepository followerRepository) {
        boolean isFollowing = authenticatedUser != null &&
                followerRepository.findById(new FollowerId(authenticatedUser.getUserId(), user.getUserId())).isPresent(); // check if the authenticatedUser follow the other user
        return new SimpleUserResponse(user.getUserId(), user.getUsername(), isFollowing);
    }
}