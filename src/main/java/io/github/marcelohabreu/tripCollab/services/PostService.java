package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.CreatePostDto;
import io.github.marcelohabreu.tripCollab.dtos.PostResponse;
import io.github.marcelohabreu.tripCollab.dtos.UpdatePostDto;
import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.exceptions.post.PostNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.repositories.PostRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.github.marcelohabreu.tripCollab.utils.AuthUtil.checkOwnership;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> createPost(CreatePostDto p, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(UserNotFoundException::new);
        Post newPost = new Post();
        newPost.setTitle(p.title());
        newPost.setBody(p.body());
        newPost.setLocation(p.location());
        newPost.setUser(user);

        postRepository.save(newPost);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Post created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updatePost(UUID id, UpdatePostDto dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        var post = checkPostExists(id);
        checkOwnership(token, post.getUser().getUserId());

        if (dto.title() != null && !dto.title().isBlank()) {
            post.setTitle(dto.title());
        }
        if (dto.body() != null && !dto.body().isBlank()) {
            post.setBody(dto.body());
        }
        if (dto.location() != null && !dto.location().isBlank()) {
            post.setLocation(dto.location());
        }

        postRepository.save(post);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post updated successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private Post checkPostExists(UUID id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deletePost(UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        var post = checkPostExists(id);
        checkOwnership(token, post.getUser().getUserId());

        postRepository.delete(post);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Post deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    public ResponseEntity<List<PostResponse>> listPosts() {
        List<PostResponse> posts = postRepository.findAll().stream().map(PostResponse::fromModel).toList();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
