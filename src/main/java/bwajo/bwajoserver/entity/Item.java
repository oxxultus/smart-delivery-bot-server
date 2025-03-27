package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ITEMS")
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "STOCKQUANTITY")
    private Long stockQuantity;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "UNIQUEVALUE")
    private String uniqueValue;
}
