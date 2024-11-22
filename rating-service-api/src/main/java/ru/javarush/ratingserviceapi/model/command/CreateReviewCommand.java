package ru.javarush.ratingserviceapi.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReviewCommand {
    private UUID content_id;
    private UUID user_id;
    private int rating;
    private String comment;

    @JsonCreator
    public CreateReviewCommand(@JsonProperty("content_id") UUID content_id,
                               @JsonProperty("user_id") UUID user_id,
                               @JsonProperty("rating")int rating,
                               @JsonProperty("comment")String comment) {
        this.content_id = content_id;
        this.user_id = user_id;
        this.rating = rating;
        this.comment = comment;
    }

}
