package kr.co.moviepassservice.service.movie;

import kr.co.moviepassservice.domain.movie.Rating;

import java.util.List;

public interface RatingService {

    Rating saveRating(Long movieId, Long memberId, Double score, String comment);
    
    List<Rating> getMovieRatings(Long movieId);
    
    List<Rating> getMemberRatings(Long memberId);
    
    Rating getRatingById(Long id);
    
    Rating updateRating(Long ratingId, Double score, String comment);
    
    void deleteRating(Long ratingId);
    
    Double getAverageRatingForMovie(Long movieId);
    
    boolean hasUserRatedMovie(Long movieId, Long memberId);
}