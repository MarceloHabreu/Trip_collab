package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.post.*;
import io.github.marcelohabreu.tripCollab.dtos.post.comment.CommentCreateRequest;
import io.github.marcelohabreu.tripCollab.dtos.post.comment.PublicCommentsResponse;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.CommentService;
import io.github.marcelohabreu.tripCollab.services.LikeService;
import io.github.marcelohabreu.tripCollab.services.PostService;
import io.github.marcelohabreu.tripCollab.services.SaveService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tripcollab/posts")
public class PostController {

    private final PostService service;
    private final LikeService likeService;
    private final SaveService saveService;
    private final CommentService commentService;

    public PostController(PostService service, LikeService likeService, SaveService saveService, CommentService commentService) {
        this.service = service;
        this.likeService = likeService;
        this.saveService = saveService;
        this.commentService = commentService;
    }

//  @ModelAttribute é usado para pegar dados do formulário, como título, corpo e localização.
//  @RequestParam é utilizado para capturar os arquivos enviados.
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostOperationResponse> create(@Valid @ModelAttribute PostCreateRequest dto, @RequestPart(value = "image", required = false) MultipartFile[] images, JwtAuthenticationToken token) throws IOException {
        return service.createPost(dto,images, token);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<SimplePostResponse>> listAdminPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.getAdminPosts(PageRequest.of(page, size));
    }


    @GetMapping("/public")
    public ResponseEntity<List<SimplePostResponse>> listPublicPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.getPublicPosts(PageRequest.of(page, size));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostOperationResponse> updateMyPost(@PathVariable UUID id, @Valid @ModelAttribute PostUpdateRequest dto, @RequestPart(value = "image", required = false) MultipartFile[] images, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.updateMyPost(id, dto,images, token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getMyPost(@PathVariable UUID id, JwtAuthenticationToken token, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.getMyPost(id, token, PageRequest.of(page, size));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable UUID id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.getPublicPost(id, PageRequest.of(page, size));
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

    // Comments
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable UUID postId, @Valid @RequestBody CommentCreateRequest comment, JwtAuthenticationToken token) {
        return commentService.createComment(postId, comment, token);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID commentId, JwtAuthenticationToken token) {
        return commentService.deleteComment(commentId, token);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<PublicCommentsResponse> listCommentsByPost(@PathVariable UUID postId) {
        return commentService.getCommentsByPosts(postId);
    }


}
