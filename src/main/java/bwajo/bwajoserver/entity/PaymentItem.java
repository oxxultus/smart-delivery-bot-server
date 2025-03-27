package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PAYMENTITEMS")
public class PaymentItem {

    @Id
    @GeneratedValue
    private Long id;
    private int quantity;
    private Long totalPrice;


    @ManyToOne
    @JoinColumn(name = "PAYMENTLIST_ID")
    private PaymentList paymentList;

   @ManyToOne
   @JoinColumn(name = "ITEM_ID")
    private Item item;
}
