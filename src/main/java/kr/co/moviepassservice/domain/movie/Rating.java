package kr.co.moviepassservice.domain.movie;

import jakarta.persistence.*;
import kr.co.moviepassservice.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Double score; // 1.0 ~ 5.0 별점

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public Rating(Movie movie, Member member, Double score, String comment) {
        this.movie = movie;
        this.member = member;
        this.score = score;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void updateRating(Double score, String comment) {
        this.score = score;
        this.comment = comment;
        this.updatedAt = LocalDateTime.now();
    }
}