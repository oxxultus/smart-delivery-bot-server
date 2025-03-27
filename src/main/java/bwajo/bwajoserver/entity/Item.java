package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ITEMS")
public class Item {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;
    private String category;
    private String uniqueValue;
}
