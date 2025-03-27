package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CARTITEMS")
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "TOTALPRICE")
    private Long totalPrice;

    @ManyToOne
    @JoinColumn(name = "CARTLIST_ID")
    private CartList cartList;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}
