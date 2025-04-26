package io.github.marcelohabreu.tripCollab.utils;

import io.github.marcelohabreu.tripCollab.entities.Comment;
import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.entities.User;
import io.github.marcelohabreu.tripCollab.exceptions.post.PostNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.post.comment.CommentNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.repositories.CommentRepository;
import io.github.marcelohabreu.tripCollab.repositories.PostRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Utility class for authentication and authorization checks
 */
@Component
public class AuthUtil{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public AuthUtil(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Checks that the user authenticated on the token is the owner of the resource identified by the ID and throw error otherwise.
     *
     * @param token The JWT authentication token.
     * @param userId The UUID of the user to be verified with the token.
     * @throws CustomAccessDeniedException If the token is null or the user is not the owner of the resource.
     * @throws IllegalArgumentException If token.getName() cannot be converted into a UUID.
     */
    public void checkOwnership(JwtAuthenticationToken token, UUID userId) throws CustomAccessDeniedException {
        if (token == null) {
            throw new CustomAccessDeniedException("Authentication required!");
        }
        try{
            UUID authenticatedUserId = UUID.fromString(token.getName());
            if (!authenticatedUserId.equals(userId)) {
                throw new CustomAccessDeniedException("You can only view or make changes on your own account! Please try again");
            }
        } catch (IllegalArgumentException e){
            throw new CustomAccessDeniedException("Invalid token format: user ID must be a valid UUID");
        }

    }
    /**
     * Checks that the user authenticated on the token is the owner of the resource identified by the ID and return false otherwise
     *
     * @param token The JWT authentication token.
     * @param userId The UUID of the user to be verified with the token.
     * @return True if the resource owns the user, false otherwise
     */
    public boolean hasOwnership(JwtAuthenticationToken token, UUID userId) {
        if (token == null) {
            return false;
        }
        try{
            UUID authenticatedUserId = UUID.fromString(token.getName());
            return authenticatedUserId.equals(userId);
        } catch (IllegalArgumentException e){
            return false;
        }

    }

    /**
     * Check if the post exists and return it if it does
     *
     * @param postId The UUID of the resource to be verified.
     * @throws PostNotFoundException If the post doesn't exist
     */
    public Post checkPostExists(UUID postId) {
        if (postId == null) {
            throw new IllegalArgumentException("The provided ID must not be null.");
        }
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    /**
     * Check if the user exists and return it if it does
     *
     * @param userId The UUID of the resource to be verified.
     * @throws UserNotFoundException If the user doesn't exist
     */
    public User checkUserExists(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("The provided ID must not be null.");
        }
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Check if the comment exists and return it if it does
     *
     * @param commentId The UUID of the resource to be verified.
     * @throws CommentNotFoundException If the comment doesn't exist
     */
    public Comment checkCommentExists(UUID commentId){
        if (commentId == null){
            throw new IllegalArgumentException("The provided ID must not be null.");
        }
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }
}

