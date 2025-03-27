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
    @Column(name = "ROLE")
    private Role role;

    @Column(name = "EMAIL", unique = true, nullable = true)
    private String email;

    @Column(name = "PASSWORD", nullable = true)
    private String password;

    @OneToOne(mappedBy = "user")
    private CartList cartlist;

    @OneToMany(mappedBy = "user")
    private List<PaymentList> paymentlist = new ArrayList<>();
}
