package kr.co.moviepassservice.repository.movie;

import kr.co.moviepassservice.domain.member.Member;
import kr.co.moviepassservice.domain.movie.Movie;
import kr.co.moviepassservice.domain.movie.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByMovie(Movie movie);
    
    List<Rating> findByMember(Member member);
    
    Optional<Rating> findByMovieAndMember(Movie movie, Member member);
    
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.movie.id = :movieId")
    Double findAverageScoreByMovieId(@Param("movieId") Long movieId);
}