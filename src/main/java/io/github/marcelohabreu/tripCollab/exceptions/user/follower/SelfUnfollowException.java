package io.github.marcelohabreu.tripCollab.exceptions.user.follower;

public class SelfUnfollowException extends RuntimeException {
    public SelfUnfollowException() {
        super("Cannot unfollow yourself");
    }
}
