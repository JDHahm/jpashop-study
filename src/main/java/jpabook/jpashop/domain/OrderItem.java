package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int count; // 주문횟수

    private int orderPrice; // 주문가격

    protected OrderItem(){

    }

    //==생성 로직==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //주문 아이템 생성할때 재고를 까준다
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        //취소된 상품 원복
        getItem().addStock(count);

    }

    //==조회 로직==//

    /**
     *
     * 주문 상품 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
