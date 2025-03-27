package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.Item;
import bwajo.bwajoserver.entity.User;

public interface CartListService {

    // 장바구니에 상품 추가
    ResultMessage addItem(User user, Item item, int quantity, Long totalPrice);

    // 장바구니에서 상품 삭제
    ResultMessage deleteItem(User user, Item item);

    // 결제
    ResultMessage payment(User user);
}
