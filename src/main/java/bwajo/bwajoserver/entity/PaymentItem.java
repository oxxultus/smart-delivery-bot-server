package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
public class PaymentItem {

    @Id
    private Long id;
    private int quantity;
    private Long totalPrice;


    @ManyToOne
    private PaymentList paymentList;

   @ManyToOne
    private Item item;
}
