package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "PAYMENTLISTS")
public class PaymentList {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENTSTATUS")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "item")
    private List<PaymentItem> paymentItems = new ArrayList<>();
}
