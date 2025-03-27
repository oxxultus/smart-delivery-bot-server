package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.Item;

import java.util.List;

public interface ItemService {

    // 상품 등록
    ResultMessage addItem(String name, Long price, Long stockQuantity, String category, String uniqueValue);

    // 상품 갱신
    ResultMessage updateItem(String name, Long price, Long stockQuantity, String category, String uniqueValue);

    // 상품 삭제
    ResultMessage deleteItem(String uniqueValue);

    // 모든 상품 정보
    List<Item> findAllItems();
}
