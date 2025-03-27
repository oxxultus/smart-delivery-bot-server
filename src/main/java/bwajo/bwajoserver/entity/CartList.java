package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class CartList {

    @Id
    private long id;

    @OneToOne
    private User users;

    @OneToMany
    private List<CartItem> cartItems = new ArrayList<>();
}
