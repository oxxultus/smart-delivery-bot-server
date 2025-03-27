package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "CARTITEMS")
public class CartItem {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "TOTALPRICE")
    private Long totalPrice;

    // 연관관계를 설정하기 위한 부분 ⬇ ==================================================

    // 양방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @ManyToOne
    @JoinColumn(name = "CARTLIST_ID")
    private CartList cartList;

    // 단방향 연관관계 입니다
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    public void setCartList(CartList cartList) {
        this.cartList = cartList;
        if (cartList.getCartItems() == null) {
            cartList.setCartItems(new ArrayList<>());  // cartItems가 null일 경우 초기화
        }
        if (!cartList.getCartItems().contains(this)) {
            cartList.getCartItems().add(this);  // 반대편 연관관계 설정
        }
    }

    // 그 외의 Getter / Setter 등등 ⬇ ==================================================


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartList getCartList() {
        return cartList;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
