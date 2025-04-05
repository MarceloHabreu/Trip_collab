package io.github.marcelohabreu.tripCollab.utils;

import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.exceptions.post.PostNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.repositories.PostRepository;
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

    @Autowired
    public AuthUtil(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Checks that the user authenticated on the token is the owner of the resource identified by the ID.
     *
     * @param token The JWT authentication token.
     * @param id The UUID of the resource to be verified.
     * @throws CustomAccessDeniedException If the token is null or the user is not the owner of the resource.
     * @throws IllegalArgumentException If token.getName() cannot be converted into a UUID.
     */
    public void checkOwnership(JwtAuthenticationToken token, UUID id) throws CustomAccessDeniedException {
        if (token == null) {
            throw new CustomAccessDeniedException("Authentication required!");
        }
        try{
            UUID authenticatedUserId = UUID.fromString(token.getName());
            if (!authenticatedUserId.equals(id)) {
                throw new CustomAccessDeniedException("You can only view or make changes on your own account! Please try again");
            }
        } catch (IllegalArgumentException e){
            throw new CustomAccessDeniedException("Invalid token format: user ID must be a valid UUID");
        }

    }

    /**
     * Check if the post exists and return it if it does
     *
     * @param id The UUID of the resource to be verified.
     * @throws PostNotFoundException If the post doesn't exist
     */
    public Post checkPostExists(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("The provided ID must not be null.");
        }
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }
}

