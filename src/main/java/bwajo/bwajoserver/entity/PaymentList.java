package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "PAYMENTLISTS")
public class PaymentList {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENTSTATUS")
    private PaymentStatus paymentStatus;

    // 연관관계를 설정하기 위한 부분 ⬇ ==================================================

    // 무결성을 위해 양방향 설정을 추가해야 함
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "paymentList" , cascade = CascadeType.ALL)
    private List<PaymentItem> paymentItems = new ArrayList<>();
}
