package ru.javarush.ratingserviceapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.javarush.ratingserviceapi.model.command.CreateReviewCommand;
import ru.javarush.ratingserviceapi.model.command.UpdateReviewCommand;
import ru.javarush.ratingserviceapi.model.dto.PageDTO;
import ru.javarush.ratingserviceapi.model.dto.ReviewByContentDto;
import ru.javarush.ratingserviceapi.model.dto.ReviewByUserDto;
import ru.javarush.ratingserviceapi.model.dto.ReviewDTO;
import ru.javarush.ratingserviceapi.model.entity.Review;

import java.util.UUID;

public interface ReviewService {

    Page<Review> getReviewsByUserId(UUID userId, Pageable pageable);

    Page<Review> getReviewsByContentId(UUID contentId, Pageable pageable);

    Float getAverageRatingByContentId(UUID contentId);

    ReviewDTO createReview(CreateReviewCommand createReviewCommand);

    ReviewDTO updateReview(UUID reviewId, UpdateReviewCommand updateCommand);

    void deleteReviewById(UUID reviewId);

//    PageDTO<ReviewByUserDto> getContentByUserId(UUID userId, Pageable pageable);
//    PageDTO<ReviewByContentDto> getContentByContentId(UUID contentId, Pageable pageable);

}
