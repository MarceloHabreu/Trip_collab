package io.github.marcelohabreu.tripCollab.exceptions;

import io.github.marcelohabreu.tripCollab.exceptions.post.PostNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.post.comment.CommentNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.AlreadyFollowingException;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.NotFollowingException;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.SelfFollowException;
import io.github.marcelohabreu.tripCollab.exceptions.post.image.FileIsNotImageException;
import io.github.marcelohabreu.tripCollab.exceptions.post.image.ImageSizeExceededException;
import io.github.marcelohabreu.tripCollab.exceptions.post.like.PostAlreadyLikedException;
import io.github.marcelohabreu.tripCollab.exceptions.post.like.PostNotLikedException;
import io.github.marcelohabreu.tripCollab.exceptions.post.save.PostAlreadySavedException;
import io.github.marcelohabreu.tripCollab.exceptions.post.save.PostNotSavedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.*;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.SelfUnfollowException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // constant formater for timestamp
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    // method to get the timestamp
    public String getCurrentTimestamp() {
        return FORMATTER.format(Instant.now());
    }

    // Validations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        response.put("errors", errors);
        response.put("timestamp", getCurrentTimestamp());
        // 422 -> HttpStatus request valid but data invalid
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    // User
    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(CustomBadCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleCustomAccessDeniedException(CustomAccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistyException(EmailAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Post
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePostNotFoundException(PostNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Image
    @ExceptionHandler(FileIsNotImageException.class)
    public ResponseEntity<Map<String, Object>> handleFileIsNotImageException(FileIsNotImageException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    @ExceptionHandler(ImageSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleImageSizeExceededException(ImageSizeExceededException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    // Like
    @ExceptionHandler(PostAlreadyLikedException.class)
    public ResponseEntity<Map<String, Object>> handlePostAlreadyLikedException(PostAlreadyLikedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PostNotLikedException.class)
    public ResponseEntity<Map<String, Object>> handlePostNotLikedException(PostNotLikedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Save
    @ExceptionHandler(PostAlreadySavedException.class)
    public ResponseEntity<Map<String, Object>> handlePostAlreadySavedExceptionn(PostAlreadySavedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PostNotSavedException.class)
    public ResponseEntity<Map<String, Object>> handlePostNotSavedException(PostNotSavedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Comment
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCommentNotFoundException(CommentNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Follower
    @ExceptionHandler(SelfFollowException.class)
    public ResponseEntity<Map<String, Object>> handleSelfFollowException(SelfFollowException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SelfUnfollowException.class)
    public ResponseEntity<Map<String, Object>> handleSelfUnfollowException(SelfUnfollowException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AlreadyFollowingException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyFollowingException(AlreadyFollowingException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFollowingException.class)
    public ResponseEntity<Map<String, Object>> handleNoExistingFollowException(NotFollowingException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Generics
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "An unexpected error occurred.");
        response.put("details", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
