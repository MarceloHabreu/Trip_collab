package io.github.marcelohabreu.tripCollab.exceptions.post.save;

public class PostAlreadySavedException extends RuntimeException {
    public PostAlreadySavedException() {
        super("This post has already been saved by the user!");
    }
}
