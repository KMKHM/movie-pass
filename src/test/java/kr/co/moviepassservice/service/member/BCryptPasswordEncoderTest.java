package kr.co.moviepassservice.service.member;


import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class BCryptPasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void password_encryption_test() {
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String encodedPassword2 = passwordEncoder.encode(rawPassword);


        // 암호화가 잘 되었는지 테스트
        assertThat(rawPassword).isNotEqualTo(encodedPassword);
        // 암호화된 결과가 길이가 60인지 확인
        assertThat(encodedPassword).hasSize(60);
        // 암호화된 비밀번호가 원래 비밀번호와 일치하는지 테스트
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        // 첫 번째 암호화된 비밀번호가 두 번째 암호화된 비밀번호와 다른지 테스트
        assertThat(encodedPassword).isNotEqualTo(encodedPassword2);
    }

}
