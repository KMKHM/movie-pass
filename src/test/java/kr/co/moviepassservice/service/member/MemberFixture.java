package kr.co.moviepassservice.service.member;

import kr.co.moviepassservice.domain.member.Gender;
import kr.co.moviepassservice.domain.member.Member;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpRequest;

/**
 * 회원 관련 테스트를 위한 픽스처 클래스
 * 테스트에 필요한 객체를 생성하는 메서드를 제공
 */
public class MemberFixture {

    private MemberFixture() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }

    /**
     * 기본 회원 가입 요청 객체 생성
     */
    public static SignUpRequest.SignUpRequestBuilder defaultSignUpRequest() {
        return SignUpRequest.builder()
                .userId("testuser")
                .password("Password123!")
                .username("홍길동")
                .email("test@example.com")
                .phoneNumber("010-1234-5678")
                .gender(Gender.MALE);
    }

    /**
     * 기본 회원 엔티티 생성
     */
    public static Member.MemberBuilder defaultMember() {
        return Member.builder()
                .userId("testuser")
                .password("Password123!")
                .username("홍길동")
                .email("test@example.com")
                .phoneNumber("010-1234-5678")
                .point(0)
                .gender(Gender.MALE);
    }

    /**
     * ID가 설정된 회원 엔티티 생성
     */
    public static Member memberWithId(Long id) {
        Member member = defaultMember().build();
        setMemberId(member, id);
        return member;
    }

    /**
     * 리플렉션을 사용하여 Member 엔티티의 ID 설정
     * 테스트 목적으로만 사용
     */
    private static void setMemberId(Member member, Long id) {
        try {
            java.lang.reflect.Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set member ID", e);
        }
    }
}
