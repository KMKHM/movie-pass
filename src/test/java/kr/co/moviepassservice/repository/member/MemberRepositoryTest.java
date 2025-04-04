package kr.co.moviepassservice.repository.member;

import kr.co.moviepassservice.domain.member.Gender;
import kr.co.moviepassservice.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("findByUserId: 존재하는 userId로 조회 시 회원을 반환한다")
    void findByUserId_WithExistingId_ShouldReturnMember() {
        // Given
        String userId = "testUser";
        Member member = createMember(userId, "test@example.com");
        memberRepository.save(member);

        // When
        Optional<Member> foundMember = memberRepository.findByUserId(userId);

        // Then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("findByUserId: 존재하지 않는 userId로 조회 시 빈 Optional을 반환한다")
    void findByUserId_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Given
        String userId = "notExistentUser";

        // When
        Optional<Member> foundMember = memberRepository.findByUserId(userId);

        // Then
        assertThat(foundMember).isEmpty();
    }

    @Test
    @DisplayName("findByEmail: 존재하는 email로 조회 시 회원을 반환한다")
    void findByEmail_WithExistingEmail_ShouldReturnMember() {
        // Given
        String email = "test@example.com";
        Member member = createMember("testUser", email);
        memberRepository.save(member);

        // When
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("findByEmail: 존재하지 않는 email로 조회 시 빈 Optional을 반환한다")
    void findByEmail_WithNonExistingEmail_ShouldReturnEmptyOptional() {
        // Given
        String email = "nonexistent@example.com";

        // When
        Optional<Member> foundMember = memberRepository.findByEmail(email);

        // Then
        assertThat(foundMember).isEmpty();
    }

    @Test
    @DisplayName("save: 새로운 회원을 저장하면 ID가 생성된다")
    void save_NewMember_ShouldGenerateId() {
        // Given
        Member member = createMember("newUser", "new@example.com");

        // When
        Member savedMember = memberRepository.save(member);

        // Then
        assertThat(savedMember.getId()).isNotNull();
    }

    @Test
    @DisplayName("save: 회원 정보를 수정하고 저장하면 변경된 정보가 반영된다")
    void save_UpdatedMember_ShouldReflectChanges() {
        // Given
        Member member = createMember("updateuser", "update@example.com");
        Member savedMember = memberRepository.save(member);
        
        String updatedPhoneNumber = "010-9876-5432";
        String updatedUsername = "수정된사용자";
        savedMember.update(updatedPhoneNumber, updatedUsername, savedMember.getPassword());

        // When
        Member updatedMember = memberRepository.save(savedMember);
        
        // Then
        assertThat(updatedMember.getPhoneNumber()).isEqualTo(updatedPhoneNumber);
        assertThat(updatedMember.getUsername()).isEqualTo(updatedUsername);
        
        // DB에서 다시 조회해서 확인
        Member retrievedMember = memberRepository.findById(updatedMember.getId()).orElseThrow();
        assertThat(retrievedMember.getPhoneNumber()).isEqualTo(updatedPhoneNumber);
        assertThat(retrievedMember.getUsername()).isEqualTo(updatedUsername);
    }

    @Test
    @DisplayName("delete: 회원을 삭제하면 더 이상, 해당 ID로 조회되지 않는다")
    void delete_Member_ShouldRemoveFromDatabase() {
        // Given
        Member member = createMember("deleteuser", "delete@example.com");
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();
        
        // When
        memberRepository.delete(savedMember);
        
        // Then
        Optional<Member> deletedMember = memberRepository.findById(memberId);
        assertThat(deletedMember).isEmpty();
    }

    @Test
    @DisplayName("중복 userId: 같은 userId를 가진 회원을 저장할 수 없다")
    void save_DuplicateUserId_ShouldFail() {
        // Given
        String duplicateUserId = "duplicate";
        Member firstMember = createMember(duplicateUserId, "first@example.com");
        memberRepository.save(firstMember);
        
        Member secondMember = createMember(duplicateUserId, "second@example.com");
        
        // When & Then
        try {
            memberRepository.save(secondMember);
            memberRepository.flush(); // 즉시 DB에 반영하여 제약 조건 위반 확인
            
            // 여기까지 도달하면 테스트 실패
            assertThat(false).isTrue(); // 의도적인 실패
        } catch (Exception e) {
            // 제약 조건 위반 예외가 발생해야 함
            assertThat(e).isNotNull();
        }
    }

    @Test
    @DisplayName("중복 email: 같은 email을 가진 회원을 저장할 수 없다")
    void save_DuplicateEmail_ShouldFail() {
        // Given
        String duplicateEmail = "duplicate@example.com";
        Member firstMember = createMember("user1", duplicateEmail);
        memberRepository.save(firstMember);
        
        Member secondMember = createMember("user2", duplicateEmail);
        
        // When & Then
        assertThatThrownBy(() -> {
           memberRepository.save(secondMember);
           memberRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
    
    /**
     * 테스트용 회원 엔티티 생성
     */
    private Member createMember(String userId, String email) {
        return Member.builder()
                .userId(userId)
                .password("Password123!")
                .username("테스트유저")
                .email(email)
                .phoneNumber("010-1234-5678")
                .point(0)
                .gender(Gender.MALE)
                .build();
    }
}
