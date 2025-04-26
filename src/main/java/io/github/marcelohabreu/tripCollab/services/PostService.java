package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.post.*;
import io.github.marcelohabreu.tripCollab.dtos.post.comment.PublicCommentsResponse;
import io.github.marcelohabreu.tripCollab.entities.Image;
import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.exceptions.post.image.FileIsNotImageException;
import io.github.marcelohabreu.tripCollab.exceptions.post.image.ImageSizeExceededException;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.repositories.*;
import io.github.marcelohabreu.tripCollab.utils.AuthUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Validated
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final CloudinaryService cloudinaryService;
    private final AuthUtil authUtil;

    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository, CommentRepository commentRepository, ImageRepository imageRepository, CloudinaryService cloudinaryService, AuthUtil authUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.imageRepository = imageRepository;
        this.cloudinaryService = cloudinaryService;
        this.authUtil = authUtil;
    }

    // Methods to get count and resources
    public PostLikeResponse fetchLikesForPost(Post post, Pageable pageable) {
        int likeCount = post.getLikeCount();
        var likedUsers = likeRepository.findLikersByPostId(post.getPostId(), pageable);
        return new PostLikeResponse(likeCount, likedUsers.map(UserShortResponse::fromModel).toList());

    }

    public PublicCommentsResponse fetchCommentsForPost(Post post, Pageable pageable) {
        int commentCount = post.getCommentCount();
        var commentPage = commentRepository.findCommentByPostId(post.getPostId(), pageable);
        return PublicCommentsResponse.fromModel(commentCount, commentPage.getContent());

    }


    @Transactional
    public ResponseEntity<PostOperationResponse> createPost(PostCreateRequest p, MultipartFile[] images, JwtAuthenticationToken token) throws IOException {
        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(UserNotFoundException::new);
        Post newPost = new Post();
        newPost.setTitle(p.title());
        newPost.setBody(p.body());
        newPost.setLocation(p.location());
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setUpdatedAt(LocalDateTime.now());

        postRepository.save(newPost);

        List<Image> imagesEntities = new ArrayList<>();

        for (MultipartFile i : images) {
            if (!i.getContentType().startsWith("image/")) {
                throw new FileIsNotImageException();
            }
            long maxSize = 2 * 1024 * 1024;
            if (i.getSize() > maxSize) {
                throw new ImageSizeExceededException();
            }
            String imageUrl = cloudinaryService.uploadImage(i, String.valueOf(user.getUserId()));

            Image newImage = new Image();
            newImage.setPost(newPost);
            newImage.setImageUrl(imageUrl);

            imagesEntities.add(newImage);
        }

        imageRepository.saveAll(imagesEntities);

        var response = new PostOperationResponse("Post created successfully", SimplePostResponse.fromModel(newPost));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional
    public ResponseEntity<PostOperationResponse> updateMyPost(UUID postId, PostUpdateRequest dto, MultipartFile[] images, JwtAuthenticationToken token) {
        var post = authUtil.checkPostExists(postId);
        authUtil.checkOwnership(token, post.getUser().getUserId());

        boolean updated = false;
        if (dto.title() != null && !dto.title().isBlank()) {
            post.setTitle(dto.title());
            updated = true;
        }
        if (dto.body() != null && !dto.body().isBlank()) {
            post.setBody(dto.body());
            updated = true;
        }
        if (dto.location() != null && !dto.location().isBlank()) {
            post.setLocation(dto.location());
            updated = true;
        }

        if (dto.imagesToRemove() != null && !dto.imagesToRemove().isEmpty()){
            List<Image> imagesToDelete = imageRepository.findAllById(dto.imagesToRemove());
            for (Image i : imagesToDelete) {
                try {
                    cloudinaryService.deleteImage(i.getImageUrl());
                    imageRepository.delete(i);
                    updated = true;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete image", e);
                }
            }
        }

        if (images != null && images.length > 0) {
            try {
                List<Image> imagesList = new ArrayList<>();
                for (MultipartFile i : images) {
                    if (!i.getContentType().startsWith("image/")) {
                        throw new FileIsNotImageException();
                    }
                    long maxSize = 2 * 1024 * 1024;
                    if (i.getSize() > maxSize) {
                        throw new ImageSizeExceededException();
                    }
                    String imageUrl = cloudinaryService.uploadImage(i, String.valueOf(post.getUser().getUserId()));

                    Image newImage = new Image();
                    newImage.setPost(post);
                    newImage.setImageUrl(imageUrl);

                    imagesList.add(newImage);
                }

                imageRepository.saveAll(imagesList);
                updated = true;
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }


        if (updated) {
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }

        var response = new PostOperationResponse(
                updated ? "Post updated successfully" : "No changes made",
                SimplePostResponse.fromModel(post)
        );

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Void> deleteMyPost(UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        var post = authUtil.checkPostExists(id);
        authUtil.checkOwnership(token, post.getUser().getUserId());

        postRepository.delete(post);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    public ResponseEntity<PostResponse> getMyPost(UUID postId, JwtAuthenticationToken token, Pageable pageable) {
        var post = authUtil.checkPostExists(postId);
        authUtil.checkOwnership(token, post.getUser().getUserId());


        // Likes and Comments
        var likes = fetchLikesForPost(post, pageable);
        var comments = fetchCommentsForPost(post, pageable);

        return ResponseEntity.ok(PostResponse.fromModel(post, likes, comments));
    }

    public ResponseEntity<PostResponse> getPublicPost(UUID id, Pageable pageable) {
        var post = authUtil.checkPostExists(id);

        // Likes and Comments
        var likes = fetchLikesForPost(post, pageable);
        var comments = fetchCommentsForPost(post, pageable);

        return ResponseEntity.ok(PostResponse.fromModel(post, likes, comments));
    }

    public ResponseEntity<List<SimplePostResponse>> getAdminPosts(Pageable pageable) {
        return ResponseEntity.ok(postRepository.findAll(pageable).stream().sorted(Comparator.comparing(Post::getTitle)).map(SimplePostResponse::fromModel).toList());
    }

    public ResponseEntity<List<SimplePostResponse>> getPublicPosts(Pageable pageable) {
        return ResponseEntity.ok(postRepository.findAll(pageable).stream().sorted(Comparator.comparing(Post::getTitle)).map(SimplePostResponse::fromModel).toList());
    }

}
