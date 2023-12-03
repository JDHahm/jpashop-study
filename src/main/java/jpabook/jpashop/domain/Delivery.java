package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

@Entity
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // 배송 상태 [READY, COMP]

    @Embedded
    private Address address;

}
