package bwajo.bwajoserver.entity;


import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "USERS")
public class User {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @Column(name = "EMAIL", unique = true, nullable = true)
    private String email;

    @Column(name = "PASSWORD", nullable = true)
    private String password;

    // 연관관계를 설정하기 위한 부분 ⬇ ==================================================

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CartList cartlist;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<PaymentList> paymentlist = new ArrayList<>();
}
