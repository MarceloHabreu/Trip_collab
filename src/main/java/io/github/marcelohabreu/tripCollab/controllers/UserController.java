package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.post.like.UserLikedPostsResponse;
import io.github.marcelohabreu.tripCollab.dtos.post.save.UserSavedPostsResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.PublicUserResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.UserUpdateRequest;
import io.github.marcelohabreu.tripCollab.dtos.user.AdminUserResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.UserResponse;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.LikeService;
import io.github.marcelohabreu.tripCollab.services.SaveService;
import io.github.marcelohabreu.tripCollab.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tripcollab/users")
public class UserController {

    private final UserService service;
    private final LikeService likeService;
    private final SaveService saveService;

    public UserController(UserService service, LikeService likeService, SaveService saveService) {
        this.service = service;
        this.likeService = likeService;
        this.saveService = saveService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<AdminUserResponse>> listAdminUsers(){
        return service.listAdminUsers();
    }

    @GetMapping("/public")
    public ResponseEntity<List<PublicUserResponse>> listPublicUsers(){
        return service.listPublicUsers();
    }

    @GetMapping("/me/profile")
    public ResponseEntity<UserResponse> getMyProfile( JwtAuthenticationToken token){
        return service.getMyProfile( token);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PublicUserResponse> getUser(@PathVariable UUID id){
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
    public ResponseEntity<UserLikedPostsResponse> listLikedPosts(JwtAuthenticationToken token) {
        return likeService.getLikedPosts(token);
    }

    // Saves
    @GetMapping("/me/saved-posts")
    public ResponseEntity<UserSavedPostsResponse> listSavedPosts(JwtAuthenticationToken token) {
        return saveService.getSavedPosts(token);
    }
}
