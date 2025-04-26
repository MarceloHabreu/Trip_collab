package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.post.comment.CommentCreateRequest;
import io.github.marcelohabreu.tripCollab.dtos.post.comment.PublicCommentsResponse;
import io.github.marcelohabreu.tripCollab.dtos.post.comment.UserCommentsResponse;
import io.github.marcelohabreu.tripCollab.entities.Comment;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.repositories.CommentRepository;
import io.github.marcelohabreu.tripCollab.repositories.PostRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import io.github.marcelohabreu.tripCollab.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AuthUtil authUtil;

    public CommentService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.authUtil = authUtil;
    }

    @Transactional
    public ResponseEntity<Void> createComment(UUID postId, CommentCreateRequest dtoComment, JwtAuthenticationToken token) {
        var post = authUtil.checkPostExists(postId);
        var user = userRepository.findById(UUID.fromString(token.getName())).orElseThrow(UserNotFoundException::new);


        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(dtoComment.content());
        commentRepository.save(comment);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteComment(UUID commentId, JwtAuthenticationToken token) {
        var comment = authUtil.checkCommentExists(commentId);

        // check if the user who wants to delete the comment is the owner of the comment or the owner of the post
        boolean isOwnerOfComment = authUtil.hasOwnership(token, comment.getUser().getUserId());
        boolean isOwnerOfPost = authUtil.hasOwnership(token, comment.getPost().getUser().getUserId());

        if (isOwnerOfComment || isOwnerOfPost) {
            commentRepository.delete(comment);

            return ResponseEntity.noContent().build();
        } else {
            throw new CustomAccessDeniedException("You're not authorized to delete this comment.");
        }
    }

    public ResponseEntity<UserCommentsResponse> getMyComments(JwtAuthenticationToken token) {
        UUID userId = UUID.fromString(token.getName());
        var comments = commentRepository.findCommentsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(UserCommentsResponse.fromModel(comments));
    }

    public ResponseEntity<PublicCommentsResponse> getCommentsByPosts(UUID postId){
        List<Comment> comments = commentRepository.findCommentsByPostId(postId).stream().sorted(Comparator.comparing(Comment::getCreatedAt)).toList();
        int countComments = commentRepository.countCommentsByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(PublicCommentsResponse.fromModel(countComments,comments));
    }

}
