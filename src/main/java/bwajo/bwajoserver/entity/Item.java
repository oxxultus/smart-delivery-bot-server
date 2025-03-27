package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ITEMS")
public class Item {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
