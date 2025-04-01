package kr.co.moviepassservice.service.member;

import kr.co.moviepassservice.domain.member.Member;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpRequest;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpResponse;
import kr.co.moviepassservice.exception.BusinessException;
import kr.co.moviepassservice.exception.ErrorCode;
import kr.co.moviepassservice.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kr.co.moviepassservice.service.member.MemberFixture.defaultMember;
import static kr.co.moviepassservice.service.member.MemberFixture.defaultSignUpRequest;
import static kr.co.moviepassservice.service.member.MemberFixture.memberWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Nested
    @DisplayName("회원 가입 테스트")
    class SignUpTest {
        
        @Test
        @DisplayName("회원 가입 성공 - 유효한 요청으로 회원 가입이 정상 처리됨")
        void signUp_WithValidRequest_ShouldRegisterMember() {
            // Given
            final SignUpRequest request = defaultSignUpRequest().build();
            final Member member = memberWithId(1L);
            given(memberRepository.findByUserId(request.getUserId())).willReturn(Optional.empty());
            given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
            given(memberRepository.save(any(Member.class))).willReturn(member);

            // When
            SignUpResponse response = memberService.signUp(request);

            // Then
            verify(memberRepository, times(1)).findByUserId(request.getUserId());
            verify(memberRepository, times(1)).findByEmail(request.getEmail());
            verify(memberRepository, times(1)).save(any(Member.class));
            
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(member.getId());
            assertThat(response.getUserId()).isEqualTo(member.getUserId());
            assertThat(response.getUsername()).isEqualTo(member.getUsername());
            assertThat(response.getEmail()).isEqualTo(member.getEmail());
            
            // 저장되는 Member 객체의 값 검증
            ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
            verify(memberRepository).save(memberCaptor.capture());
            Member savedMember = memberCaptor.getValue();
            
            assertThat(savedMember.getUserId()).isEqualTo(request.getUserId());
            assertThat(savedMember.getPassword()).isEqualTo(request.getPassword());
            assertThat(savedMember.getUsername()).isEqualTo(request.getUsername());
            assertThat(savedMember.getEmail()).isEqualTo(request.getEmail());
            assertThat(savedMember.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
            assertThat(savedMember.getGender()).isEqualTo(request.getGender());
            assertThat(savedMember.getPoint()).isEqualTo(0); // 초기 포인트는 0
        }

        @Test
        @DisplayName("회원 가입 실패 - 이미 존재하는 아이디로 가입 시도")
        void signUp_WithDuplicateUserId_ShouldThrowException() {
            // Given
            final SignUpRequest request = defaultSignUpRequest().build();
            final Member existingMember = defaultMember().build();
            given(memberRepository.findByUserId(request.getUserId())).willReturn(Optional.of(existingMember));

            // When & Then
            assertThatThrownBy(() -> memberService.signUp(request))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_USER_ID);
            
            verify(memberRepository, times(1)).findByUserId(request.getUserId());
            verify(memberRepository, times(0)).findByEmail(request.getEmail());
            verify(memberRepository, times(0)).save(any(Member.class));
        }

        @Test
        @DisplayName("회원 가입 실패 - 이미 존재하는 이메일로 가입 시도")
        void signUp_WithDuplicateEmail_ShouldThrowException() {
            // Given
            final SignUpRequest request = defaultSignUpRequest().build();
            final Member existingMember = defaultMember().build();
            given(memberRepository.findByUserId(request.getUserId())).willReturn(Optional.empty());
            given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(existingMember));

            // When & Then
            assertThatThrownBy(() -> memberService.signUp(request))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_EMAIL);
            
            verify(memberRepository, times(1)).findByUserId(request.getUserId());
            verify(memberRepository, times(1)).findByEmail(request.getEmail());
            verify(memberRepository, times(0)).save(any(Member.class));
        }
    }
    
    @Nested
    @DisplayName("도메인 테스트")
    class DomainTest {
        
        @Test
        @DisplayName("포인트 추가 - 정상적으로 포인트가 추가됩니다")
        void addPoint_ShouldAddPointCorrectly() {
            // Given
            Member member = defaultMember().point(100).build();
            int additionalPoint = 50;
            
            // When
            member.addPoint(additionalPoint);
            
            // Then
            assertThat(member.getPoint()).isEqualTo(150);
        }
        
        @Test
        @DisplayName("회원 정보 업데이트 - 정상적으로 회원 정보가 업데이트됩니다")
        void update_ShouldUpdateMemberInfoCorrectly() {
            // Given
            Member member = defaultMember().build();
            String newPhoneNumber = "010-9876-5432";
            String newUsername = "김철수";
            String newPassword = "NewPassword456!";
            
            // When
            member.update(newPhoneNumber, newUsername, newPassword);
            
            // Then
            assertThat(member.getPhoneNumber()).isEqualTo(newPhoneNumber);
            assertThat(member.getUsername()).isEqualTo(newUsername);
            assertThat(member.getPassword()).isEqualTo(newPassword);
            
            // 다른 정보는 변경되지 않아야 합니다
            assertThat(member.getUserId()).isEqualTo("testuser");
            assertThat(member.getEmail()).isEqualTo("test@example.com");
        }
    }
}
