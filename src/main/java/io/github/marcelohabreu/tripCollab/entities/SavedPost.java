package io.github.marcelohabreu.tripCollab.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_savedposts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavedPost {
    @Id
    @GeneratedValue (strategy = GenerationType .UUID)
    @Column(name = "savedpost_id")
    private UUID  savedPostId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(name = "saved_at", updatable = false, nullable = false, insertable = false)
    private LocalDateTime savedAt;

    @Column(name = "save_count")
    private int saveCount;
}
