package ru.javarush.ratingserviceapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.javarush.ratingserviceapi.model.entity.Review;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findAllByUserId(UUID userId, Pageable pageable);

    Page<Review> findAllByContentId(UUID contentId, Pageable pageable);

    boolean existsById(UUID reviewId);

    void deleteById(UUID reviewId);

    Optional<Review> findById(UUID reviewId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.contentId = :contentId")
    Float findAverageRatingByContentId(UUID contentId);

}
