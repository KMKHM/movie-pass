package kr.co.moviepassservice.domain.movie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String director;

    @Column(nullable = false)
    private Integer runtime; // 상영 시간(분)

    private LocalDate releaseDate;

    @Column(length = 500)
    private String posterUrl;

    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @Column(nullable = false)
    private boolean isScreening;

    @Builder
    public Movie(String title, String description, String director, Integer runtime,
                LocalDate releaseDate, String posterUrl, AgeRating ageRating, boolean isScreening) {
        this.title = title;
        this.description = description;
        this.director = director;
        this.runtime = runtime;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.ageRating = ageRating;
        this.isScreening = isScreening;
    }

    // 평점 추가 메서드
    public void addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setMovie(this);
    }

    // 카테고리 추가 메서드
    public void addCategory(Category category) {
        this.categories.add(category);
        category.getMovies().add(this);
    }

    // 평점 평균 계산 메서드
    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .mapToDouble(Rating::getScore)
                .average()
                .orElse(0.0);
    }
}