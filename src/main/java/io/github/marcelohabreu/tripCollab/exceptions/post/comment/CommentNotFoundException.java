package io.github.marcelohabreu.tripCollab.exceptions.post.comment;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException() {
    super("Comment not found! Please try again.");
  }
}
