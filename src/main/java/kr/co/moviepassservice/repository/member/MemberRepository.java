package kr.co.moviepassservice.repository.member;

import kr.co.moviepassservice.domain.member.Gender;
import kr.co.moviepassservice.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);
    
    // 추가 쿼리 메서드
    List<Member> findByGender(Gender gender);
    
    Page<Member> findByUsernameContaining(String username, Pageable pageable);
    
    @Query("SELECT m FROM Member m WHERE m.point > :minPoint")
    List<Member> findMembersWithPointGreaterThan(@Param("minPoint") Integer minPoint);
    
    @Query("SELECT m FROM Member m WHERE m.username LIKE %:keyword% OR m.email LIKE %:keyword%")
    List<Member> searchMembers(@Param("keyword") String keyword);
}
