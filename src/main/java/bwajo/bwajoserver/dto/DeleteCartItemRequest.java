package bwajo.bwajoserver.dto;

public class DeleteCartItemRequest {

    private Long cartItemId;
    private BodyItem itemDetails;  // 아이템 세부 정보를 받는 필드

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public BodyItem getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(BodyItem itemDetails) {
        this.itemDetails = itemDetails;
    }
}