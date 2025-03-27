package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class PaymentList {

    @Id
    private Long id;
    private PaymentStatus paymentStatus;

    @ManyToOne
    private User user;

    @OneToMany
    private List<PaymentItem> paymentItems = new ArrayList<>();
}
