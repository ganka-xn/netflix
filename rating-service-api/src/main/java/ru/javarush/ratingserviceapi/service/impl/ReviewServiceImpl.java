package ru.javarush.ratingserviceapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javarush.ratingserviceapi.exceptions.ObjectNotFoundException;
import ru.javarush.ratingserviceapi.mapping.ReviewMapper;
import ru.javarush.ratingserviceapi.model.command.CreateReviewCommand;
import ru.javarush.ratingserviceapi.model.command.UpdateReviewCommand;
import ru.javarush.ratingserviceapi.model.dto.PageDTO;
import ru.javarush.ratingserviceapi.model.dto.ReviewByContentDto;
import ru.javarush.ratingserviceapi.model.dto.ReviewByUserDto;
import ru.javarush.ratingserviceapi.model.dto.ReviewDTO;
import ru.javarush.ratingserviceapi.model.entity.Review;
import ru.javarush.ratingserviceapi.repository.ReviewRepository;
import ru.javarush.ratingserviceapi.service.ReviewService;

import java.util.UUID;

//todo организовать логирование

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public Page<Review> getReviewsByUserId(UUID userId, Pageable pageable) {
        log.info("Getting reviews for user with id: {}", userId);

        Page<Review> reviews = reviewRepository.findAllByUserId(userId, pageable);
        log.info("Retrieved {} reviews for user id: {}", reviews.getTotalElements(), userId);

        return reviews;
    }

//    @Override
//    public PageDTO<ReviewByUserDto> getContentByUserId(UUID userId, Pageable pageable) {
//        return reviewRepository.findAllByUserId(userId, pageable);
//    }

    @Override
    public Page<Review> getReviewsByContentId(UUID contentId, Pageable pageable) {
        log.info("Getting reviews for content with id: {}", contentId);

        Page<Review> reviews = reviewRepository.findAllByContentId(contentId, pageable);
        log.info("Retrieved {} reviews for content id: {}", reviews.getTotalElements(), contentId);

        return reviews;
    }

//    @Override
//    public PageDTO<ReviewByContentDto> getContentByContentId(UUID contentId, Pageable pageable) {
//        return reviewRepository.findAllByContentId(contentId, pageable);
//    }

    @Override
    public Float getAverageRatingByContentId(UUID contentId) {
        log.info("Getting average rating for content with id: {}", contentId);
        Float averageRating = reviewRepository.findAverageRatingByContentId(contentId);

        if (averageRating == null) {
            log.warn("No reviews found for content id: {}", contentId);
            throw new ObjectNotFoundException("No reviews found for content id: " + contentId);
        }

        log.info("Successfully calculated average rating for content id {}: {}", contentId, averageRating);
        return averageRating;
    }

//    public Float getAverageRatingByContentId(UUID contentId) {
//        log.info("Getting average rating for content with id: {}", contentId);
//        return reviewRepository.findAverageRatingByContentId(contentId);
//    }

    @Override
    public ReviewDTO createReview(CreateReviewCommand createReviewCommand) {
        log.debug("Creating review for command: {}", createReviewCommand);
        var reviewToCreate = reviewMapper.toEntityWithDate(createReviewCommand);

        try {
            var savedReview = reviewRepository.save(reviewToCreate);
            log.info("Review {} saved successfully", savedReview.getId());
            return reviewMapper.toDTO(savedReview);
        } catch (Exception e) {
            log.error("Error saving review: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(UUID reviewId, UpdateReviewCommand updateCommand) {

        log.debug("Updating review with id: {}, command: {}", reviewId, updateCommand);
        var requestedReview = reviewRepository.findById(reviewId).orElse(null);

        if (requestedReview == null) {
            log.warn("Attempted to update non-existent review with id: {}", reviewId);
            throw new ObjectNotFoundException("Review with id " + reviewId + " not found");
        }

        log.info("Found review with id {}: {}", reviewId, requestedReview);
        var reviewToUpdate = reviewMapper.toEntity(updateCommand, requestedReview);

        log.debug("Updating review from {} to {}", requestedReview, reviewToUpdate);
        reviewRepository.save(reviewToUpdate);

        log.info("Review with id {} has been successfully updated to {}", reviewId, reviewToUpdate);
        return reviewMapper.toDTO(reviewToUpdate);
    }

    @Override
    @Transactional
    public void deleteReviewById(UUID reviewId) {
        log.debug("Received request to delete review with id: {}", reviewId);

        if (!reviewRepository.existsById(reviewId)) {
            log.warn("Attempted to delete non-existent review with id: {}", reviewId);
            throw new RuntimeException("Отзыв " + reviewId + " не найден");
        }

        reviewRepository.deleteById(reviewId);
        log.info("Review with id {} has been successfully deleted", reviewId);
    }

}
