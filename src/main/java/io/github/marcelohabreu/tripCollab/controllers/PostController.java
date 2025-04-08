package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.post.*;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.LikeService;
import io.github.marcelohabreu.tripCollab.services.PostService;
import io.github.marcelohabreu.tripCollab.services.SaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tripcollab/posts")
public class PostController {

    private final PostService service;
    private final LikeService likeService;
    private final SaveService saveService;

    public PostController(PostService service, LikeService likeService, SaveService saveService) {
        this.service = service;
        this.likeService = likeService;
        this.saveService = saveService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody PostCreateRequest dto, JwtAuthenticationToken token) {
        return service.createPost(dto, token);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<PostAdminResponse>> listAdminPosts() {
        return service.listAdminPosts();
    }


    @GetMapping("/public")
    public ResponseEntity<List<PublicPostResponse>> listPublicPosts() {
        return service.listPublicPosts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMyPost(@PathVariable UUID id, @Valid @RequestBody PostUpdateRequest dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.updateMyPost(id, dto, token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getMyPost(@PathVariable UUID id, JwtAuthenticationToken token) {
        return service.getMyPost(id, token);
    }
    @GetMapping("/public/{id}")
    public ResponseEntity<PublicPostResponse> getPost(@PathVariable UUID id) {
        return service.getPublicPost(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyPost(@PathVariable UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.deleteMyPost(id, token);
    }

    // Likes
    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, JwtAuthenticationToken token) {
        return likeService.likePost(postId, token);
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, JwtAuthenticationToken token) {
        return likeService.unlikePost(postId, token);
    }

    // Saves
    @PostMapping("/{postId}/saves")
    public ResponseEntity<Void> savePost(@PathVariable UUID postId, JwtAuthenticationToken token) {
        return saveService.savePost(postId, token);
    }

    @DeleteMapping("/{postId}/saves")
    public ResponseEntity<Void> unsavePost(@PathVariable UUID postId, JwtAuthenticationToken token) {
        return saveService.unsavePost(postId, token);
    }


}
