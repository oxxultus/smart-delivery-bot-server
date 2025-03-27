package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CARTITEMS")
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    private int quantity;
    private Long totalPrice;

    @ManyToOne
    @JoinColumn(name = "CARTLIST_ID")
    private CartList cartList;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
}
