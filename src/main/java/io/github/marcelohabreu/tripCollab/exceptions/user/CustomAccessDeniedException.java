package io.github.marcelohabreu.tripCollab.exceptions.user;

public class CustomAccessDeniedException extends RuntimeException {
    public CustomAccessDeniedException(String message) {
        super(message);
    }
}
