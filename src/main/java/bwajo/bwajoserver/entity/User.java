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

    // 영방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CartList cartList;

    // 영방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PaymentList> paymentLists = new ArrayList<>();

    public void setCartList(CartList cartList) {
        this.cartList = cartList;
        if (cartList != null && cartList.getUser() != this) {
            cartList.setUser(this);  // 반대편 연관관계 설정
        }
    }
    public void addPaymentList(PaymentList paymentList) {
        if (paymentLists == null) {
            paymentLists = new ArrayList<>(); // null일 경우 초기화
        }
        paymentLists.add(paymentList);
        if (paymentList.getUser() != this) {
            paymentList.setUser(this);  // 반대편 연관관계 설정
        }
    }
}
