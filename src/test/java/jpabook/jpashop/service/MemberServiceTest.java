package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional // 롤백함
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepositroy memberRepositroy;

    @Test
    @DisplayName("회원가입 테스트")
//    @Rollback(value = false) // 이걸 넣으면 롤백 안하고 커밋함, insert문 들어가는걸 볼수있음
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        Member foundMember = memberRepositroy.findOne(savedId);

        //then

        Assertions.assertThat(member).isEqualTo(foundMember);

    }

    @Test
    @DisplayName("중복 회원 등록시" +
            "" +
            " 예외 터짐")
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when

        memberService.join(member1);

        //then
//        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        Assertions.assertThatThrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class);
    }
}