package io.github.marcelohabreu.tripCollab.exceptions.post.save;

public class PostNotSavedException extends RuntimeException {
    public PostNotSavedException() {
        super("This post has not been saved by the user!");
    }
}
