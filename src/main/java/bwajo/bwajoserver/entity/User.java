package bwajo.bwajoserver.entity;


import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String password;

    @OneToOne(mappedBy = "user")
    private CartList cartlist;

    @OneToMany(mappedBy = "user")
    private List<PaymentList> paymentlist = new ArrayList<>();
}
