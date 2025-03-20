package io.github.marcelohabreu.tripCollab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table (name = "tb_post_images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID image_id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String image_url;
    private LocalDateTime uploaded_at;
}