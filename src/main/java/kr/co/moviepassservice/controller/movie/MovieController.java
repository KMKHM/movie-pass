package kr.co.moviepassservice.controller.movie;

import kr.co.moviepassservice.domain.movie.Movie;
import kr.co.moviepassservice.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.saveMovie(movie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/screening")
    public ResponseEntity<List<Movie>> getCurrentlyScreeningMovies() {
        return ResponseEntity.ok(movieService.getCurrentlyScreeningMovies());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(title));
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Movie>> getMoviesByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(movieService.getMoviesByCategory(categoryName));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Movie>> getTopRatedMovies() {
        return ResponseEntity.ok(movieService.getTopRatedMovies());
    }

    @GetMapping("/new-releases")
    public ResponseEntity<List<Movie>> getNewReleases(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(movieService.getNewReleases(days));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
//        movie.setId(id); // 요청 경로의 ID를 설정
        movieService.updateMovie(movie);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}