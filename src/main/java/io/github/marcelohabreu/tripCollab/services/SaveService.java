package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.post.save.UserSavedPostsResponse;
import io.github.marcelohabreu.tripCollab.entities.SavedPost;
import io.github.marcelohabreu.tripCollab.exceptions.post.save.PostAlreadySavedException;
import io.github.marcelohabreu.tripCollab.exceptions.post.save.PostNotSavedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.repositories.SaveRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import io.github.marcelohabreu.tripCollab.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SaveService {
    private final SaveRepository saveRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public SaveService(SaveRepository saveRepository, PostService postService, UserRepository userRepository, AuthUtil authUtil) {
        this.saveRepository = saveRepository;
        this.postService = postService;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }


    @Transactional
    public ResponseEntity<Void> savePost(UUID postId, JwtAuthenticationToken token) {
        var post = authUtil.checkPostExists(postId);
        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(UserNotFoundException::new);

        if (saveRepository.existsByPostPostIdAndUserUserId(postId, user.getUserId())) {
            throw new PostAlreadySavedException();
        }

        SavedPost savedPost = new SavedPost();
        savedPost.setPost(post);
        savedPost.setUser(user);

        saveRepository.save(savedPost);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> unsavePost(UUID postId, JwtAuthenticationToken token) {
        authUtil.checkPostExists(postId);
        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(UserNotFoundException::new);

        var postSaved = saveRepository.findByPostPostIdAndUserUserId(postId, user.getUserId()).orElseThrow(PostNotSavedException::new);
        saveRepository.delete(postSaved);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<UserSavedPostsResponse> getSavedPosts(JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(token.getName());
        var posts = saveRepository.findPostsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(UserSavedPostsResponse.fromModel(posts));
    }

}
