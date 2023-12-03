package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 읽기전용에서 성능최적화 시켜줌
@RequiredArgsConstructor
public class MemberService {

    // Setter 인젝션 -> 생성자 인젝션 -> 하나밖에 없을땐 Autowired 생략 -> AllArgsConstructor -> RequiredArgsConstructor
    // 결과적으로는 생성자 인젝션임
    private final MemberRepositroy memberRepositroy;



    /*
    회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복회원 감증
        memberRepositroy.save(member);
        return member.getId();

    }

    private void validateDuplicateMember(Member member) {
        List<Member> foundmember = memberRepositroy.findByName(member.getName());
        if (!foundmember.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    public List<Member> findMembers() {
        return memberRepositroy.findALl();
    }

    public Member findOne(Long memberId) {
        return memberRepositroy.findOne(memberId);
    }
}
