package io.github.marcelohabreu.tripCollab.dtos.auth;

public record TokenDetails(String token, Long expiresIn) {
}