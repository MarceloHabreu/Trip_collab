package io.github.marcelohabreu.tripCollab.dtos.post;

import io.github.marcelohabreu.tripCollab.entities.User;

import java.util.UUID;

public record UserShortResponse(UUID userId, String username){
    public static UserShortResponse fromModel(User u){
        return new UserShortResponse(u.getUserId(), u.getUsername());
    }
}