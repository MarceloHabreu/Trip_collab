package io.github.marcelohabreu.tripCollab.exceptions.post.like;

public class AlertAddLikePostException extends RuntimeException {
    public AlertAddLikePostException() {
        super("You've already liked this post!");
    }
}
