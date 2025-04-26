package io.github.marcelohabreu.tripCollab.dtos.post;


import java.util.List;

public record PostLikeResponse(int countLikes, List<UserShortResponse> likedByUsers){
}