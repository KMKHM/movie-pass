package kr.co.moviepassservice.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Builder
    public Member(String userId, String password, String phoneNumber, String username, 
                 String email, Integer point, Gender gender) {
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.email = email;
        this.point = point;
        this.gender = gender;
    }
    
    /**
     * 회원 정보 업데이트
     * @param phoneNumber 전화번호
     * @param username 사용자명
     * @param password 비밀번호
     */
    public void update(String phoneNumber, String username, String password) {
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
    }
    
    /**
     * 포인트 업데이트
     * @param point 변경할 포인트
     */
    public void updatePoint(Integer point) {
        this.point = point;
    }
    
    /**
     * 포인트 추가
     * @param additionalPoint 추가할 포인트
     */
    public void addPoint(Integer additionalPoint) {
        this.point += additionalPoint;
    }
}
