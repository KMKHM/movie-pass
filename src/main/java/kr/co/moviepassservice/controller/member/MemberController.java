package kr.co.moviepassservice.controller.member;

import kr.co.moviepassservice.dto.member.MemberDto;
import kr.co.moviepassservice.dto.member.MemberDto.SignUpResponse;
import kr.co.moviepassservice.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/api/members")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody MemberDto.SignUpRequest request) {
        MemberDto.SignUpResponse response = memberService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
