package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.services.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/tripcollab/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable UUID postId, JwtAuthenticationToken token){
        return likeService.likePost(postId, token);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable UUID postId, JwtAuthenticationToken token){
        return likeService.unlikePost(postId, token);
    }
}
