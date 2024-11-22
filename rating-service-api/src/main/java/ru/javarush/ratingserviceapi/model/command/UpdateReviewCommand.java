package ru.javarush.ratingserviceapi.model.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateReviewCommand {

    private int rating;
    private String comment;

    @JsonCreator
    public UpdateReviewCommand(@JsonProperty("rating") int rating,
                               @JsonProperty("comment") String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
