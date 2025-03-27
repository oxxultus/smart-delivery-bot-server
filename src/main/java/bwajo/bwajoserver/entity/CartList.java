package bwajo.bwajoserver.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "CARTLISTS")
public class CartList {

    // 일반 컬럼을 설정하기 위한 부분 ⬇ =================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 연관관계를 설정하기 위한 부분 ⬇ ==================================================

    // 양방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    // 영방향 연관관계 입니다.
    // 무결성을 위해 양방향 설정을 추가해야 함 (완료)
    @OneToMany(mappedBy = "cartList" , cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        if (user.getCartList() != this) {
            user.setCartList(this);  // 반대편 연관관계 설정
        }
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);  // null 체크 없이 바로 추가
        if (cartItem.getCartList() != this) {
            cartItem.setCartList(this);  // 반대편 연관관계 설정
        }
    }

    // 그 외의 Getter / Setter 등등 ⬇ ==================================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
