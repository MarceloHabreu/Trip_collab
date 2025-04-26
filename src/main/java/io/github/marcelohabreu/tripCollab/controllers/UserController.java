package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.post.comment.UserCommentsResponse;
import io.github.marcelohabreu.tripCollab.dtos.post.like.UserLikedPostsResponse;
import io.github.marcelohabreu.tripCollab.dtos.post.save.UserSavedPostsResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.*;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tripcollab/users")
public class UserController {

    private final UserService service;
    private final LikeService likeService;
    private final SaveService saveService;
    private final CommentService commentService;
    private final FollowerService followerService;

    public UserController(UserService service, LikeService likeService, SaveService saveService, CommentService commentService, FollowerService followerService) {
        this.service = service;
        this.likeService = likeService;
        this.saveService = saveService;
        this.commentService = commentService;
        this.followerService = followerService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<AdminUserResponse>> listAdminUsers() {
        return service.listAdminUsers();
    }

    @GetMapping("/public")
    public ResponseEntity<List<SimpleUserResponse>> listPublicUsers(Principal authenticatedUser) {
        return service.listPublicUsers(authenticatedUser);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(JwtAuthenticationToken token) {
        return service.getMyProfile(token);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<OtherUserResponse> getUser(@PathVariable UUID id) {
        return service.getPublicUser(id);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<Map<String, Object>> updateMyProfile(@Valid @RequestBody UserUpdateRequest dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.updateMyProfile(token, dto);
    }

    @DeleteMapping("/me/profile")
    public ResponseEntity<Void> deleteMyProfile(JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.deleteMyProfile(token);
    }

    // Likes
    @GetMapping("/me/liked-posts")
    public ResponseEntity<UserLikedPostsResponse> getMyLikedPosts(JwtAuthenticationToken token) {
        return likeService.getLikedPosts(token);
    }

    // Saves
    @GetMapping("/me/saved-posts")
    public ResponseEntity<UserSavedPostsResponse> getMySavedPosts(JwtAuthenticationToken token) {
        return saveService.getSavedPosts(token);
    }

    // Comments
    @GetMapping("/me/comments")
    public ResponseEntity<UserCommentsResponse> getMyComments(JwtAuthenticationToken token) {
        return commentService.getMyComments(token);
    }

     // Follower
    @PostMapping("/{userId}/follow")
    public ResponseEntity<Void> followUser(@PathVariable UUID userId, Principal authenticatedUser){
        return followerService.followUser(userId, authenticatedUser);
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable UUID userId, Principal authenticatedUser){
        return followerService.unfollowUser(userId, authenticatedUser);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<SimpleUserResponse>> listFollowers(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Principal principal) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return followerService.getFollowers(userId, principal, pageRequest);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<SimpleUserResponse>> listFollowing(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Principal principal) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return followerService.getFollowing(userId, principal, pageRequest);
    }

}
