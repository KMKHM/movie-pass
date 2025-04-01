package kr.co.moviepassservice.service.movie;

import kr.co.moviepassservice.domain.movie.Movie;
import kr.co.moviepassservice.repository.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getCurrentlyScreeningMovies() {
        return movieRepository.findByIsScreeningTrue();
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContaining(title);
    }

    @Override
    public List<Movie> getMoviesByCategory(String categoryName) {
        return movieRepository.findByCategoriesName(categoryName);
    }

    @Override
    public List<Movie> getTopRatedMovies() {
        return movieRepository.findTopRatedMovies();
    }

    @Override
    public List<Movie> getNewReleases(int days) {
        LocalDate date = LocalDate.now().minusDays(days);
        return movieRepository.findNewMovies(date);
    }

    @Override
    @Transactional
    public void updateMovie(Movie movie) {
        // 존재하는지 확인 후 업데이트
        if (movieRepository.existsById(movie.getId())) {
            movieRepository.save(movie);
        } else {
            throw new IllegalArgumentException("Movie not found with id: " + movie.getId());
        }
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}