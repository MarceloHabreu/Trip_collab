package io.github.marcelohabreu.tripCollab.exceptions.user.follower;

public class NotFollowingException extends RuntimeException {
    public NotFollowingException() {
        super("You are not following this user yet");
    }
}
