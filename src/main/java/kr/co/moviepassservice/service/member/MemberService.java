package kr.co.moviepassservice.service.member;

import kr.co.moviepassservice.dto.member.MemberDto.SignUpRequest;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpResponse;

public interface MemberService {
    /**
     * 회원 가입
     * @param request 회원 가입 요청 정보
     * @return 회원 가입 결과
     */
    SignUpResponse signUp(SignUpRequest request);
}
