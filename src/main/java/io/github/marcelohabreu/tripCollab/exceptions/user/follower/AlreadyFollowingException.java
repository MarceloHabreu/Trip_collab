package io.github.marcelohabreu.tripCollab.exceptions.user.follower;

public class AlreadyFollowingException extends RuntimeException {
    public AlreadyFollowingException() {
        super("You are already following this user");
    }
}
