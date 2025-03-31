package io.github.marcelohabreu.tripCollab.exceptions.post;

public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException() {
    super("Post not found! Please try again.");
  }
}
