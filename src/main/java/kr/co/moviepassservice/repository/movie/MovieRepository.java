package kr.co.moviepassservice.repository.movie;

import kr.co.moviepassservice.domain.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByIsScreeningTrue();
    
    List<Movie> findByTitleContaining(String title);
    
    List<Movie> findByReleaseDateBetween(LocalDate start, LocalDate end);
    
    List<Movie> findByCategoriesName(String categoryName);
    
    @Query("SELECT m FROM Movie m JOIN m.ratings r GROUP BY m ORDER BY AVG(r.score) DESC")
    List<Movie> findTopRatedMovies();
    
    @Query("SELECT m FROM Movie m WHERE m.releaseDate >= :date AND m.isScreening = true")
    List<Movie> findNewMovies(@Param("date") LocalDate date);
}