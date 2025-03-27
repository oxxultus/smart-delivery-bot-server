package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "CARTLISTS")
public class CartList {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany
    private List<CartItem> cartItems = new ArrayList<>();
}
