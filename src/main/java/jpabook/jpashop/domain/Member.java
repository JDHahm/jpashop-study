package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "member") // 나는 주인이 아니고 거울일 뿐
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private Address address;

}
