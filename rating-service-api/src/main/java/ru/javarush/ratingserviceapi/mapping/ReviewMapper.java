package ru.javarush.ratingserviceapi.mapping;

import jakarta.validation.constraints.NotNull;
import org.mapstruct.*;
import ru.javarush.ratingserviceapi.model.command.CreateReviewCommand;
import ru.javarush.ratingserviceapi.model.command.UpdateReviewCommand;
import ru.javarush.ratingserviceapi.model.dto.ReviewDTO;
import ru.javarush.ratingserviceapi.model.entity.Review;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "reviewId", source = "id")
    ReviewDTO toDTO(Review review);

    default Review toEntityWithDate(@NotNull CreateReviewCommand command) {
        Review review = new Review(
                command.getContent_id(),
                command.getUser_id(),
                command.getRating(),
                command.getComment());
        return review; // reviewDate устанавливается внутри конструктора
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review toEntity(UpdateReviewCommand updateCommand, @MappingTarget Review review);

/*
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "contentId", source = "content_id"),
            @Mapping(target = "userId", source = "user_id"),
            @Mapping(target = "reviewDate", ignore = true)
    })
    Review toEntity(CreateReviewCommand createReviewCommand);
*/

}
