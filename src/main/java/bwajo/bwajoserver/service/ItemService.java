package bwajo.bwajoserver.service;

import bwajo.bwajoserver.entity.Item;

import java.util.List;

public interface ItemService {

    // 상품 등록
    String addItem(String name, Long price, Long stockQuantity, String category, String uniqueValue);

    // 상품 갱신
    String updateItem(String name, Long price, Long stockQuantity, String category, String uniqueValue);

    // 상품 삭제
    String deleteItem(String uniqueValue);
    List<Item> findAllItems();
}
