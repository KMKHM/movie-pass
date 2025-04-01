package kr.co.moviepassservice.domain.movie;

public enum AgeRating {
    ALL("전체관람가"),
    TWELVE("12세 이상 관람가"),
    FIFTEEN("15세 이상 관람가"),
    ADULT("청소년 관람불가");

    private final String description;

    AgeRating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}