package bwajo.bwajoserver.entity;


import jakarta.persistence.*;

import java.util.*;

@Entity
public class User {

    @Id
    private Long id;
    private Role role;
    private String email;
    private String password;

    @ManyToOne
    private CartList cartlist;

    @OneToMany
    private List<PaymentList> paymentlist = new ArrayList<>();
}
