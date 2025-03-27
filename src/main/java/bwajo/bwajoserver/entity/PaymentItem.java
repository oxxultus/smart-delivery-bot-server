package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PAYMENTITEMS")
public class PaymentItem {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "TOTALPRICE")
    private Long totalPrice;

    // 연관관계를 설정하기 위한 부분 ⬇ ==================================================

    // 영방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @ManyToOne
    @JoinColumn(name = "PAYMENTLIST_ID")
    private PaymentList paymentList;

    // 단방향 연관관계 입니다.
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    public void setPaymentList(PaymentList paymentList) {
        this.paymentList = paymentList;
        if (paymentList.getPaymentItems() == null) {
            paymentList.setPaymentItems(new ArrayList<>());  // paymentItems가 null일 경우 초기화
        }
        if (!paymentList.getPaymentItems().contains(this)) {
            paymentList.getPaymentItems().add(this);  // 반대편 연관관계 설정
        }
    }
}
