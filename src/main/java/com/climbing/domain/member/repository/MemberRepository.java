package com.climbing.domain.member.repository;

import com.climbing.auth.oauth2.SocialType;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Page<Member> findByRole(Role role, Pageable pageable);
}
