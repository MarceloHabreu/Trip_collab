package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.CreatePostDto;
import io.github.marcelohabreu.tripCollab.dtos.PostResponse;
import io.github.marcelohabreu.tripCollab.dtos.UpdatePostDto;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/tripcollab/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> create(@Valid @RequestBody CreatePostDto dto, JwtAuthenticationToken token) {
        return service.createPost(dto, token);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> list() {
        return service.listPosts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> update(@PathVariable UUID id, @Valid @RequestBody UpdatePostDto dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.updatePost(id, dto, token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.deletePost(id, token);
    }
}
