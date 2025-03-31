package io.github.marcelohabreu.tripCollab.dtos.user;

import io.github.marcelohabreu.tripCollab.dtos.post.PostResponse;
import io.github.marcelohabreu.tripCollab.entities.User;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record PublicUserResponse(UUID userId, String username, int postCount) {
    public static PublicUserResponse fromModel(User u){
        Set<PostResponse> posts = u.getPosts().stream().map(PostResponse::fromModel).collect(Collectors.toSet());
        return new PublicUserResponse(u.getUserId(), u.getUsername(), posts.size());
    }
}
