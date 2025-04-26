package io.github.marcelohabreu.tripCollab.exceptions.post.image;

public class FileIsNotImageException extends IllegalArgumentException {
    public FileIsNotImageException() {
        super("The file must be an image");
    }
}
