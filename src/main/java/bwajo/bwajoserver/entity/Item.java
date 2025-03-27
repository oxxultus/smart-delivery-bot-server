package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

@Entity
public class Item {

    @Id
    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;
    private String category;
    private String uniqueValue;
}
