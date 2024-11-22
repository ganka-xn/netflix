package ru.javarush.ratingserviceapi.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewByContentDto {

    private UUID reviewId;
    private UUID userId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
}
