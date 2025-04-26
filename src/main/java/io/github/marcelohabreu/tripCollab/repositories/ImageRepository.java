package io.github.marcelohabreu.tripCollab.repositories;

import io.github.marcelohabreu.tripCollab.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
}
