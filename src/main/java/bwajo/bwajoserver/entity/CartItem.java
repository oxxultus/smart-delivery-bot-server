package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CARTITEMS")
public class CartItem {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "TOTALPRICE")
    private Long totalPrice;

    // 연관관계를 설정하기 위한 부분 ⬇ ==================================================

    // 무결성을 위해 양방향 설정을 추가해야 함
    @ManyToOne
    @JoinColumn(name = "CARTLIST_ID")
    private CartList cartList;

    // 무결성을 위해 양방향 설정을 추가해야 함
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}
