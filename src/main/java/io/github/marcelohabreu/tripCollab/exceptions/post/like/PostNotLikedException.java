package io.github.marcelohabreu.tripCollab.exceptions.post.like;

public class PostNotLikedException extends RuntimeException {
    public PostNotLikedException() {
        super("This post has not been liked by the user!");
    }
}
