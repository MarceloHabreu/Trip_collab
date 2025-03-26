package io.github.marcelohabreu.tripCollab.exceptions.user;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("The username you are trying to register already exists! Please try again.");
    }
}
