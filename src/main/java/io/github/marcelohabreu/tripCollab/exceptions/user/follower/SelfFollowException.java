package io.github.marcelohabreu.tripCollab.exceptions.user.follower;

public class SelfFollowException extends RuntimeException {
    public SelfFollowException() {
        super("Cannot follow yourself");
    }
}
