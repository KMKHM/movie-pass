package kr.co.moviepassservice.integration;

import kr.co.moviepassservice.domain.member.Gender;
import kr.co.moviepassservice.domain.member.Member;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpRequest;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpResponse;
import kr.co.moviepassservice.exception.BusinessException;
import kr.co.moviepassservice.exception.ErrorCode;
import kr.co.moviepassservice.repository.member.MemberRepository;
import kr.co.moviepassservice.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입 통합 테스트 - 회원이 성공적으로 등록되고 DB에 저장됨")
    void signUp_ShouldRegisterMemberAndSaveInDB() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .userId("newuser")
                .password("Password123!")
                .username("김통합")
                .email("integration@example.com")
                .phoneNumber("010-9876-5432")
                .gender(Gender.FEMALE)
                .build();

        // When
        SignUpResponse response = memberService.signUp(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getUserId()).isEqualTo(request.getUserId());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());

        // DB에서 조회하여 확인
        Member savedMember = memberRepository.findById(response.getId()).orElseThrow();
        assertThat(savedMember.getUserId()).isEqualTo(request.getUserId());
        assertThat(savedMember.getUsername()).isEqualTo(request.getUsername());
        assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedMember.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(savedMember.getGender()).isEqualTo(request.getGender());
        assertThat(savedMember.getPoint()).isEqualTo(0); // 초기 포인트 확인
    }

    @Test
    @DisplayName("중복 아이디 회원 가입 통합 테스트 - 이미 존재하는 아이디로 가입 시 예외 발생")
    void signUp_WithDuplicateUserId_ShouldThrowException() {
        // Given
        // 첫 번째 회원 가입
        SignUpRequest firstRequest = SignUpRequest.builder()
                .userId("dupuser")
                .password("Password123!")
                .username("첫번째회원")
                .email("first@example.com")
                .phoneNumber("010-1111-2222")
                .gender(Gender.MALE)
                .build();
        memberService.signUp(firstRequest);

        // 중복된 아이디로 두 번째 회원 가입 시도
        SignUpRequest duplicateRequest = SignUpRequest.builder()
                .userId("dupuser") // 중복 아이디
                .password("DifferentPw456!")
                .username("두번째회원")
                .email("second@example.com")
                .phoneNumber("010-3333-4444")
                .gender(Gender.FEMALE)
                .build();

        // When & Then
        assertThatThrownBy(() -> memberService.signUp(duplicateRequest))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_USER_ID);

        // DB에 첫 번째 회원만 존재해야 함
        assertThat(memberRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("중복 이메일 회원 가입 통합 테스트 - 이미 존재하는 이메일로 가입 시 예외 발생")
    void signUp_WithDuplicateEmail_ShouldThrowException() {
        // Given
        // 첫 번째 회원 가입
        SignUpRequest firstRequest = SignUpRequest.builder()
                .userId("user1")
                .password("Password123!")
                .username("첫번째회원")
                .email("duplicate@example.com") // 중복될 이메일
                .phoneNumber("010-1111-2222")
                .gender(Gender.MALE)
                .build();
        memberService.signUp(firstRequest);

        // 중복된 이메일로 두 번째 회원 가입 시도
        SignUpRequest duplicateRequest = SignUpRequest.builder()
                .userId("user2") // 다른 아이디
                .password("DifferentPw456!")
                .username("두번째회원")
                .email("duplicate@example.com") // 중복 이메일
                .phoneNumber("010-3333-4444")
                .gender(Gender.FEMALE)
                .build();

        // When & Then
        assertThatThrownBy(() -> memberService.signUp(duplicateRequest))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_EMAIL);

        // DB에 첫 번째 회원만 존재해야 함
        assertThat(memberRepository.count()).isEqualTo(1);
    }
}
