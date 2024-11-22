package ru.javarush.ratingserviceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javarush.ratingserviceapi.exceptions.ObjectNotFoundException;
import ru.javarush.ratingserviceapi.model.command.CreateReviewCommand;
import ru.javarush.ratingserviceapi.model.command.UpdateReviewCommand;
import ru.javarush.ratingserviceapi.model.dto.AverageRatingResponse;
import ru.javarush.ratingserviceapi.model.dto.ReviewDTO;
import ru.javarush.ratingserviceapi.model.entity.Review;
import ru.javarush.ratingserviceapi.service.ReviewService;

import java.util.UUID;

@Slf4j
@Tag(name = "Reviews", description = "API для работы с отзывами - ReviewController")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Получить отзывы по ID контента",
            description = "Возвращает страницу отзывов, связанных с указанным ID контента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзывы успешно получены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор контента"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/content/{contentId}")
    public Page<Review> getReviewsByContentId(
            @Parameter(description = "ID контента", required = true) @PathVariable UUID contentId,
            @Parameter(description = "Параметры пагинации") @PageableDefault Pageable pageable
    ) {
        return reviewService.getReviewsByContentId(contentId, pageable);
    }

    @Operation(
            summary = "Получить отзывы пользователя",
            description = "Возвращает страницу отзывов, оставленных пользователем с указанным userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзывы успешно получены"),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор пользователя"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user/{userId}")
    public Page<Review> getReviewsByUserId(
            @Parameter(description = "Идентификатор пользователя", required = true) @PathVariable UUID userId,
            @Parameter(description = "Параметры пагинации") @PageableDefault Pageable pageable
    ) {
        return reviewService.getReviewsByUserId(userId, pageable);
    }

    @Operation(
            summary = "Получить средний рейтинг по ID контента",
            description = "Возвращает ID контента и значение среднего рейтинга")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Средний рейтинг успешно получен"),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор контента"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/average-rating/{contentId}")
    public ResponseEntity<AverageRatingResponse> getAverageRating(
            @Parameter(description = "ID контента", required = true) @PathVariable UUID contentId) {

        try {
            Float averageRating = reviewService.getAverageRatingByContentId(contentId);
            AverageRatingResponse averageRatingResponse = new AverageRatingResponse(contentId, averageRating);

            return new ResponseEntity<>(averageRatingResponse, HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Создать новый отзыв",
            description = "Создает новый отзыв на основе предоставленных данных.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для создания отзыва"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<ReviewDTO> create(
            @Parameter(description = "Данные для создания отзыва", required = true)
            @Valid @RequestBody CreateReviewCommand createCommand
    ) {
        ReviewDTO createdReview = reviewService.createReview(createCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @Operation(
            summary = "Обновить отзыв",
            description = "Обновляет отзыв по указанному идентификатору отзыва (reviewId).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор отзыва или некорректные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "Идентификатор отзыва", required = true) @PathVariable UUID reviewId,
            @RequestBody UpdateReviewCommand updateCommand
    ) {
        var result = reviewService.updateReview(reviewId, updateCommand);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Удалить отзыв",
            description = "Удаляет отзыв по указанному идентификатору отзыва (reviewId).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отзыв успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор отзыва"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReviewById(
            @Parameter(description = "Идентификатор отзыва", required = true) @PathVariable UUID reviewId
    ) {
        reviewService.deleteReviewById(reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
