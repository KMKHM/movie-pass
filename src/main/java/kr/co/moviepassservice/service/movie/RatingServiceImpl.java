package kr.co.moviepassservice.service.movie;

import kr.co.moviepassservice.domain.member.Member;
import kr.co.moviepassservice.domain.movie.Movie;
import kr.co.moviepassservice.domain.movie.Rating;
import kr.co.moviepassservice.repository.member.MemberRepository;
import kr.co.moviepassservice.repository.movie.MovieRepository;
import kr.co.moviepassservice.repository.movie.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Rating saveRating(Long movieId, Long memberId, Double score, String comment) {
        // 영화 및 회원 존재 확인
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));
                
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        
        // 이미 평점을 남겼는지 확인
        ratingRepository.findByMovieAndMember(movie, member).ifPresent(r -> {
            throw new IllegalStateException("Member already rated this movie");
        });
        
        // 평점 유효성 검사 (1.0 ~ 5.0)
        if (score < 1.0 || score > 5.0) {
            throw new IllegalArgumentException("Rating score must be between 1.0 and 5.0");
        }
        
        Rating rating = Rating.builder()
                .movie(movie)
                .member(member)
                .score(score)
                .comment(comment)
                .build();
                
        // 영화에 평점 추가
        movie.addRating(rating);
        
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getMovieRatings(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));
        return ratingRepository.findByMovie(movie);
    }

    @Override
    public List<Rating> getMemberRatings(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        return ratingRepository.findByMember(member);
    }

    @Override
    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id: " + id));
    }

    @Override
    @Transactional
    public Rating updateRating(Long ratingId, Double score, String comment) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found with id: " + ratingId));
        
        // 평점 유효성 검사 (1.0 ~ 5.0)
        if (score < 1.0 || score > 5.0) {
            throw new IllegalArgumentException("Rating score must be between 1.0 and 5.0");
        }
        
        rating.updateRating(score, comment);
        return rating;
    }

    @Override
    @Transactional
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    @Override
    public Double getAverageRatingForMovie(Long movieId) {
        return ratingRepository.findAverageScoreByMovieId(movieId);
    }

    @Override
    public boolean hasUserRatedMovie(Long movieId, Long memberId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + movieId));
                
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
                
        return ratingRepository.findByMovieAndMember(movie, member).isPresent();
    }
}