package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래키
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // persist 가 먼지 모르겠지만 한번에 전파해줌
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // one to one 에서는 어디해도 상관없고 상황에 맞게
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //양방향 관계일 때
    /*

    이 코드를 쓸거를 아래 연관관계 메서드로 작성
    public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();
        member.getOrders().add(order);
        order.setMember(member);
    }

     */
    // ==연관관계 편입 매서드== //
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /*
    주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            //배송완료이면
            throw new IllegalStateException("이미 배송이 완료된 주문입니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /*
    상품 전체 가격 조회
     */
    public int getTotalPrice() {
        //stream으로 바꿀수도있음
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }






}
