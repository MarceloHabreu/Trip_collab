package io.github.marcelohabreu.tripCollab.dtos.post.like;


import io.github.marcelohabreu.tripCollab.dtos.post.PublicPostResponse;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.util.List;


public record UserLikedPostsResponse(int countLikedPosts, List<PublicPostResponse> likedPosts) {
    public static UserLikedPostsResponse fromModel(List<Post> p){
        return new UserLikedPostsResponse(p.size(), p.stream().map(PublicPostResponse::fromModel).toList());
    }
}
