package io.github.marcelohabreu.tripCollab.exceptions.post.image;

public class ImageSizeExceededException extends RuntimeException {
    public ImageSizeExceededException() {
        super("The file exceeds the allowed limit of 2mb");
    }
}
