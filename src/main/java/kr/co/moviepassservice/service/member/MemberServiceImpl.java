package kr.co.moviepassservice.service.member;

import kr.co.moviepassservice.domain.member.Member;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpRequest;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpResponse;
import kr.co.moviepassservice.exception.BusinessException;
import kr.co.moviepassservice.exception.ErrorCode;
import kr.co.moviepassservice.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        // 아이디 중복 확인
        validateDuplicateUserId(request.getUserId());
        
        // 이메일 중복 확인
        validateDuplicateEmail(request.getEmail());
        
        // 비밀번호 암호화 (실제 구현시 추가 필요)
        // String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        // 회원 엔티티 생성
        Member member = Member.builder()
                .userId(request.getUserId())
                .password(request.getPassword()) // 실제 구현시 암호화된 비밀번호 사용
                .phoneNumber(request.getPhoneNumber())
                .username(request.getUsername())
                .email(request.getEmail())
                .point(0) // 신규 회원은 포인트 0으로 시작
                .gender(request.getGender())
                .build();
        
        // 회원 저장
        Member savedMember = memberRepository.save(member);
        
        // 응답 생성
        return SignUpResponse.builder()
                .id(savedMember.getId())
                .userId(savedMember.getUserId())
                .username(savedMember.getUsername())
                .email(savedMember.getEmail())
                .build();
    }
    
    /**
     * 아이디 중복 검사
     * @param userId 사용자 아이디
     * @throws BusinessException 아이디가 이미 존재할 경우
     */
    private void validateDuplicateUserId(String userId) {
        Optional<Member> member = memberRepository.findByUserId(userId);
        if (member.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_USER_ID);
        }
    }
    
    /**
     * 이메일 중복 검사
     * @param email 사용자 이메일
     * @throws BusinessException 이메일이 이미 존재할 경우
     */
    private void validateDuplicateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
