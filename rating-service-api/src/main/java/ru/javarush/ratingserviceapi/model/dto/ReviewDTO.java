package ru.javarush.ratingserviceapi.model.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO implements Serializable {
    private UUID reviewId;
    private UUID contentId;
    private UUID userId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
}
