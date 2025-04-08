package io.github.marcelohabreu.tripCollab.dtos.post.save;

import io.github.marcelohabreu.tripCollab.dtos.post.PublicPostResponse;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.util.List;

public record UserSavedPostsResponse(int countSavedPosts, List<PublicPostResponse> savedPosts) {
    public static UserSavedPostsResponse fromModel(List<Post> p){
        return new UserSavedPostsResponse(p.size(), p.stream().map(PublicPostResponse::fromModel).toList());
    }
}
