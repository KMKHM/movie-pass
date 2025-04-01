package kr.co.moviepassservice.service.movie;

import kr.co.moviepassservice.domain.movie.Movie;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {

    Movie saveMovie(Movie movie);
    
    Movie getMovieById(Long id);
    
    List<Movie> getAllMovies();
    
    List<Movie> getCurrentlyScreeningMovies();
    
    List<Movie> searchMoviesByTitle(String title);
    
    List<Movie> getMoviesByCategory(String categoryName);
    
    List<Movie> getTopRatedMovies();
    
    List<Movie> getNewReleases(int days);
    
    void updateMovie(Movie movie);
    
    void deleteMovie(Long id);
}