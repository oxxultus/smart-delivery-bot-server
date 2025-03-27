package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    private Long id;

    private int quantity;
    private Long totalPrice;

    @ManyToOne
    private CartList cartList;

    @ManyToOne
    private Item item;
}
