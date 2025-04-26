package io.github.marcelohabreu.tripCollab.dtos.post.like;

import io.github.marcelohabreu.tripCollab.dtos.post.SimplePostResponse;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.util.List;


public record UserLikedPostsResponse(int countLikes, List<SimplePostResponse> likedPosts) {
    public static UserLikedPostsResponse fromModel(List<Post> p){
        return new UserLikedPostsResponse(p.size(), p.stream().map(SimplePostResponse::fromModel).toList());
    }
}
