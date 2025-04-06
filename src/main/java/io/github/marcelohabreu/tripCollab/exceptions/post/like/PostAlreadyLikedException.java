package io.github.marcelohabreu.tripCollab.exceptions.post.like;

public class PostAlreadyLikedException extends RuntimeException {
    public PostAlreadyLikedException() {
        super("This post has already been liked by the user!");
    }
}
