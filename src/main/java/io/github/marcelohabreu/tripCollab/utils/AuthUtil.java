package io.github.marcelohabreu.tripCollab.utils;

import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

/**
 * Utility class for authentication and authorization checks
 */
public class AuthUtil{
    /**
     * Checks that the user authenticated on the token is the owner of the resource identified by the ID.
     *
     * @param token The JWT authentication token.
     * @param id The UUID of the resource to be verified.
     * @throws CustomAccessDeniedException If the token is null or the user is not the owner of the resource.
     * @throws IllegalArgumentException If token.getName() cannot be converted into a UUID.
     */
    public static void checkOwnership(JwtAuthenticationToken token, UUID id) throws CustomAccessDeniedException {
        if (token == null) {
            throw new CustomAccessDeniedException("Authentication required!");
        }
        try{
            UUID authenticatedUserId = UUID.fromString(token.getName());
            if (!authenticatedUserId.equals(id)) {
                throw new CustomAccessDeniedException("You can only make changes on your own account! Please try again");
            }
        } catch (IllegalArgumentException e){
            throw new CustomAccessDeniedException("Invalid token format: user ID must be a valid UUID");
        }

    }
}

