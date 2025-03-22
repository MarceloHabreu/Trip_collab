package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Post;
import io.github.marcelohabreu.tripCollab.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
}
