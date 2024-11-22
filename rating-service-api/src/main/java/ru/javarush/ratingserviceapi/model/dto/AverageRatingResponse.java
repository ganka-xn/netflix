package ru.javarush.ratingserviceapi.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AverageRatingResponse {
    private UUID contentId;
    private float averageRating;

    public AverageRatingResponse(UUID contentId, float averageRating) {
        this.contentId = contentId;
        this.averageRating = averageRating;
    }
}
