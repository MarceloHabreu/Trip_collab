package io.github.marcelohabreu.tripCollab.exceptions.post.like;

public class AlertRemoveLikePostException extends RuntimeException {
    public AlertRemoveLikePostException() {
        super("You haven't liked this post yet!");
    }
}
