package bwajo.bwajoserver.service;

import bwajo.bwajoserver.entity.CartItem;
import bwajo.bwajoserver.entity.User;

public interface CartListService {

    // 장바구니에 상품 추가
    String addItem(User user, CartItem cartItem);

    // 장바구니에서 상품 삭제
    String deleteItem(User user, CartItem cartItem);

    // 결제
    String payment(User user);
}
