package kr.co.moviepassservice.controller.movie;

import kr.co.moviepassservice.domain.movie.Rating;
import kr.co.moviepassservice.service.movie.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Map<String, Object> payload) {
        Long movieId = Long.parseLong(payload.get("movieId").toString());
        Long memberId = Long.parseLong(payload.get("memberId").toString());
        Double score = Double.parseDouble(payload.get("score").toString());
        String comment = (String) payload.get("comment");

        Rating savedRating = ratingService.saveRating(movieId, memberId, score, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Rating>> getMovieRatings(@PathVariable Long movieId) {
        return ResponseEntity.ok(ratingService.getMovieRatings(movieId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Rating>> getMemberRatings(@PathVariable Long memberId) {
        return ResponseEntity.ok(ratingService.getMemberRatings(memberId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRating(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        Double score = Double.parseDouble(payload.get("score").toString());
        String comment = (String) payload.get("comment");

        Rating updatedRating = ratingService.updateRating(id, score, comment);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movie/{movieId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long movieId) {
        return ResponseEntity.ok(ratingService.getAverageRatingForMovie(movieId));
    }

    @GetMapping("/movie/{movieId}/member/{memberId}/has-rated")
    public ResponseEntity<Boolean> hasUserRatedMovie(
            @PathVariable Long movieId,
            @PathVariable Long memberId) {
        return ResponseEntity.ok(ratingService.hasUserRatedMovie(movieId, memberId));
    }
}