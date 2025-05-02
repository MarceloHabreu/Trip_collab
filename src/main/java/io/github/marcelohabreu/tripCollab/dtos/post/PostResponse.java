package io.github.marcelohabreu.tripCollab.dtos.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.marcelohabreu.tripCollab.dtos.post.comment.PublicCommentsResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.UserCreatorResponse;
import io.github.marcelohabreu.tripCollab.entities.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PostResponse(UUID postId,
                           String title,
                           String body,
                           String location,
                           UserCreatorResponse createdBy,
                           PostLikeResponse likes,
                           PublicCommentsResponse comments,
                           int countSaves,
                           boolean isLikedByUser,
                           boolean isSavedByUser,
                           List<Map<String, String>> images,
                           @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                           LocalDateTime createdAt,
                           @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                           LocalDateTime updatedAt) {
    public static PostResponse fromModel(Post post, PostLikeResponse likes, PublicCommentsResponse comments, boolean isLikedByUser, boolean isSavedByUser) {
        // Trazendo uma listagem de um map, que seria basicamente gerar a listagem das images levando sua chave e valor especificando cada item para consumo no front
        List<Map<String, String>> images = post.getImages()
                .stream()
                .map(image -> Map.of("imageId", image.getImageId().toString(), "imageUrl", image.getImageUrl())).toList();

        return new PostResponse(post.getPostId(), post.getTitle(),
                post.getBody(), post.getLocation(), UserCreatorResponse.fromModel(post.getUser()),
                likes,
                comments,
                post.getSaveCount(), isLikedByUser, isSavedByUser, images, post.getCreatedAt(), post.getUpdatedAt());
    }
}
