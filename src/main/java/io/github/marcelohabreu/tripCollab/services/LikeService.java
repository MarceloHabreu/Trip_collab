package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.entities.Like;
import io.github.marcelohabreu.tripCollab.exceptions.post.like.AlertAddLikePostException;
import io.github.marcelohabreu.tripCollab.exceptions.post.like.AlertRemoveLikePostException;
import io.github.marcelohabreu.tripCollab.repositories.LikeRepository;
import io.github.marcelohabreu.tripCollab.repositories.PostRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import io.github.marcelohabreu.tripCollab.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LikeService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final AuthUtil authUtil;

    public LikeService(UserRepository userRepository, PostRepository postRepository, LikeRepository likeRepository, AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.authUtil = authUtil;
    }

    @Transactional
    public ResponseEntity<Void> likePost(UUID postId, JwtAuthenticationToken token){
        var post = authUtil.checkPostExists(postId);
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var checkLikePost = likeRepository.existsByPostId(post.getPostId(), user.get().getUserId());

        if (checkLikePost){
            throw new AlertAddLikePostException();
        }

        Like likeInPost = new Like();
        likeInPost.setPost(post);
        likeInPost.setUser(user.get());

        likeRepository.save(likeInPost);

        return ResponseEntity.noContent().build();
    }
    @Transactional
    public ResponseEntity<Void> unlikePost(UUID postId, JwtAuthenticationToken token){
        var post = authUtil.checkPostExists(postId);
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var checkLikePost = likeRepository.existsByPostId(post.getPostId(), user.get().getUserId());

        if (!checkLikePost){
            throw new AlertRemoveLikePostException();
        }

        var postLiked = likeRepository.findByPostId(postId, user.get().getUserId());

        likeRepository.delete(postLiked);

        return ResponseEntity.noContent().build();
    }

}
