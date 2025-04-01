package kr.co.moviepassservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid input value"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "Internal server error"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "Resource not found"),
    
    // Member
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "M001", "User ID is already in use"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "M002", "Email is already in use"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M003", "Member not found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "M004", "Invalid password"),
    
    // Movie
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "MV001", "Movie not found"),
    INVALID_MOVIE_STATUS(HttpStatus.BAD_REQUEST, "MV002", "Invalid movie status"),
    
    // Reservation
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "Reservation not found"),
    SEAT_ALREADY_BOOKED(HttpStatus.CONFLICT, "R002", "Seat is already booked"),
    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "R003", "Invalid reservation status");
    
    private final HttpStatus status;
    private final String code;
    private final String message;
    
    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
