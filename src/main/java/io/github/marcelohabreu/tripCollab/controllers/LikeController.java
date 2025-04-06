package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.services.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/tripcollab/posts")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<Void> addLike(@PathVariable UUID postId, JwtAuthenticationToken token){
        return likeService.likePost(postId, token);
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<Void> removeLike(@PathVariable UUID postId, JwtAuthenticationToken token){
        return likeService.unlikePost(postId, token);
    }
}
