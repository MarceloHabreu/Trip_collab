package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.post.*;
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
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;

import static io.github.marcelohabreu.tripCollab.utils.AuthUtil.checkOwnership;

@Service
@Validated
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private Post checkPostExists(UUID id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> createPost(PostCreateRequest p, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(UserNotFoundException::new);
        Post newPost = new Post();
        newPost.setTitle(p.title());
        newPost.setBody(p.body());
        newPost.setLocation(p.location());
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setUpdatedAt(LocalDateTime.now());

        postRepository.save(newPost);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post created successfully");
        response.put("post", PostResponse.fromModel(newPost));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateMyPost(UUID id, PostUpdateRequest dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
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

        post.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post updated successfully");
        response.put("post", PostResponse.fromModel(post));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional
    public ResponseEntity<Void> deleteMyPost(UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        var post = checkPostExists(id);
        checkOwnership(token, post.getUser().getUserId());

        postRepository.delete(post);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<PostResponse> getMyPost(UUID id, JwtAuthenticationToken token){
        var post = checkPostExists(id);
        checkOwnership(token, post.getUser().getUserId());

        return ResponseEntity.ok(PostResponse.fromModel(post));
    }

    public ResponseEntity<PublicPostResponse> getPublicPost(UUID id){
        var post = checkPostExists(id);
        return ResponseEntity.ok(PublicPostResponse.fromModel(post));
    }


    public ResponseEntity<List<PostAdminResponse>> listAdminPosts() {
        return ResponseEntity.ok(postRepository.findAll().stream().sorted(Comparator.comparing(Post::getTitle)).map(PostAdminResponse::fromModel).toList());
    }
    public ResponseEntity<List<PublicPostResponse>> listPublicPosts() {
        return ResponseEntity.ok(postRepository.findAll().stream().sorted(Comparator.comparing(Post::getTitle)).map(PublicPostResponse::fromModel).toList());
    }
}
