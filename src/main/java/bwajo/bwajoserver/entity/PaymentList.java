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

    // 영방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    // 영방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함
    @OneToMany(mappedBy = "paymentList" , cascade = CascadeType.ALL)
    private List<PaymentItem> paymentItems = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        if (!user.getPaymentLists().contains(this)) {
            user.getPaymentLists().add(this);  // 반대편 연관관계 설정
        }
    }
    public void addPaymentItems(PaymentItem paymentItem) {
        paymentItems.add(paymentItem);
        if (paymentItem.getPaymentList() != this) {
            paymentItem.setPaymentList(this);  // 반대편 연관관계 설정
        }
    }
}
