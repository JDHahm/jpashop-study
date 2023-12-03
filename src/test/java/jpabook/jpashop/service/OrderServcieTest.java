package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServcieTest {

    @Autowired EntityManager em;
    @Autowired OrderServcie orderServcie;

    @Autowired OrderRepository orderRepository;

    @Test
    public void 주문() throws Exception {
        //given
        Member member = createMember("종덕");
        Book book = createBook("종덕 공부중", 10000, 20);
        int orderCount = 3;
        
        //when
        Long orderId = orderServcie.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(17);
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1);


    }
    
    @Test
    public void 재고수량_초과_주문_테스트() throws Exception {
        //given
        Member member = createMember("종덕");
        Book book = createBook("종덕 공부중", 10000, 20);
        //when

        int orderCount = 100;

        //then
        Assertions.assertThatThrownBy(() -> orderServcie.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);


    }

    @Test
    public void 주문_취소() throws Exception {
        //given
        Member member = createMember("제이디");
        Book book = createBook("범죄학개론", 10000, 30);
        int orderCount = 10;
        Long orderId = orderServcie.order(member.getId(), book.getId(), orderCount);

        //when
        orderServcie.cancelOrder(orderId);

        //then
        Order canceledOrder = orderRepository.findOne(orderId);
        assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCEL); // 상태 변경확인
        assertThat(book.getStockQuantity()).isEqualTo(30); // 재고 원복 확인

    }



    private Book createBook(String bookName, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(bookName);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String memberName) {
        Member member = new Member();
        member.setName(memberName);
        member.setAddress(new Address("서울", "종로", "123-0123"));
        em.persist(member);
        return member;
    }

}