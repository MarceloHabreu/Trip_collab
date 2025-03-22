package io.github.marcelohabreu.tripCollab.exceptions.user;

import org.springframework.security.authentication.BadCredentialsException;

public class CustomBadCredentialsException extends BadCredentialsException {
    public CustomBadCredentialsException() {
        super("Invalid email or password! Please try again.");
    }
}
