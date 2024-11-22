package ru.javarush.ratingserviceapi.model.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewByUserDto {

// "review_id": "UUID", "user_id": "UUID", "rating": int, "comment": "string", "review_date": "timestamp"
    private UUID reviewId;
    private UUID contentId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;

}
