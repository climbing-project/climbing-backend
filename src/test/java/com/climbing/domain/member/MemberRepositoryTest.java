package com.climbing.domain.member;

import com.climbing.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @AfterEach
    public void after() {
        em.clear();
    }

    @Test
    @DisplayName("회원 가입")
    public void saveMember() throws Exception {
        //given
        Member member = Member.builder().email("1234@1234.com").password("12341234").nickname("tiger").role(Role.USER).build();

        //when
        Member saveMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(() -> new RuntimeException("저장을 실패하였습니다."));

        assertThat(findMember).isSameAs(saveMember);
        assertThat(findMember).isSameAs(member);
    }

    @Test
    @DisplayName("중복된 이메일로 회원 가입시 오류")
    public void duplicateMember() throws Exception {
        //given
        Member member = Member.builder().email("1234@1234.com").password("12341234").nickname("tiger").role(Role.USER).build();
        Member member2 = Member.builder().email("1234@1234.com").password("12341234").nickname("lion").role(Role.USER).build();
        memberRepository.save(member);
        after();

        //when, then
        assertThrows(Exception.class, () -> memberRepository.save(member2));
    }

    @Test
    @DisplayName("멤버 찾기 테스트")
    public void findMember() throws Exception {
        //given
        String email = "1234@1234.com";
        Member member = Member.builder().email("1234@1234.com").password("12341234").nickname("tiger").role(Role.USER).build();
        memberRepository.save(member);
        after();

        //when, then
        assertThat(memberRepository.findByEmail(email).get().getEmail()).isEqualTo(member.getEmail());
        assertThat(memberRepository.findByEmail(email).get().getNickname()).isEqualTo(member.getNickname());
        assertThrows(Exception.class,
                () -> memberRepository.findByEmail("12345@1234.com").orElseThrow(Exception::new));
    }

    @Test
    @DisplayName("회원 삭제")
    public void deleteMember() throws Exception {
        //given
        Member member = Member.builder().email("1234@1234.com").password("12341234").nickname("tiger").role(Role.USER).build();
        memberRepository.save(member);
        em.clear();

        //when
        memberRepository.delete(member);


        //then
        assertThrows(Exception.class, () -> memberRepository.findById(member.getId()).orElseThrow(Exception::new));
    }

}
