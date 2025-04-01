package kr.co.moviepassservice.dto.member;

import kr.co.moviepassservice.domain.member.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
    
    @Getter
    @NoArgsConstructor
    public static class SignUpRequest {
        private String userId;
        private String password;
        private String phoneNumber;
        private String username;
        private String email;
        private Gender gender;

        @Builder
        public SignUpRequest(String userId, String password, String phoneNumber, 
                             String username, String email, Gender gender) {
            this.userId = userId;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.username = username;
            this.email = email;
            this.gender = gender;
        }
    }
    
    @Getter
    public static class SignUpResponse {
        private final Long id;
        private final String userId;
        private final String username;
        private final String email;
        
        @Builder
        public SignUpResponse(Long id, String userId, String username, String email) {
            this.id = id;
            this.userId = userId;
            this.username = username;
            this.email = email;
        }
    }
}
