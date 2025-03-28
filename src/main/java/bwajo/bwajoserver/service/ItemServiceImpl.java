package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.Item;
import bwajo.bwajoserver.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // 상품 추가
    @Override
    public ResultMessage addItem(String name, Long price, Long stockQuantity, String category, String uniqueValue) {
        // 아이템 존재 여부 체크
        Optional<Item> existingItem = itemRepository.findByUniqueValue(uniqueValue);
        if (existingItem.isPresent()) {
            // uniqueValue 중복 시 새로운 값 생성
            uniqueValue = generateUniqueValue();
        }

        // 새로운 아이템 생성
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setCategory(category);

        // 최종적으로 설정된 uniqueValue
        item.setUniqueValue(uniqueValue);

        // 아이템 저장
        itemRepository.save(item);

        return new ResultMessage(201, "아이템이 추가되었습니다.");
    }

    // uniqueValue 생성
    private String generateUniqueValue() {
        String uniqueValue;
        Optional<Item> existingItem;

        // 중복되지 않는 uniqueValue가 생성될 때까지 반복
        do {
            uniqueValue = UUID.randomUUID().toString();  // 랜덤 UUID 생성
            existingItem = itemRepository.findByUniqueValue(uniqueValue);
        } while (existingItem.isPresent());

        return uniqueValue;
    }

    // 상품 수정
    @Override
    public ResultMessage updateItem(String name, Long price, Long stockQuantity, String category, String uniqueValue) {
        Optional<Item> itemOpt = itemRepository.findByUniqueValue(uniqueValue);
        if (itemOpt.isEmpty()) {
            return new ResultMessage(404, "아이템을 찾을 수 없습니다.");
        }

        Item item = itemOpt.get();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        item.setCategory(category);

        // 아이템 저장
        itemRepository.save(item);

        return new ResultMessage(200, "아이템이 업데이트되었습니다.");
    }

    // 상품 삭제
    @Override
    public ResultMessage deleteItem(String uniqueValue) {
        Optional<Item> itemOpt = itemRepository.findByUniqueValue(uniqueValue);
        if (itemOpt.isEmpty()) {
            return new ResultMessage(404, "아이템을 찾을 수 없습니다.");
        }

        try {
            itemRepository.delete(itemOpt.get());  // 아이템 삭제
            return new ResultMessage(200, "아이템이 삭제되었습니다.");
        } catch (DataIntegrityViolationException e) {
            // 외래 키 제약 조건 위반 시 예외 처리
            return new ResultMessage(400, "해당 아이템은 다른 테이블에서 참조되고 있어 삭제할 수 없습니다.");
        } catch (Exception e) {
            // 그 외의 예외 처리
            return new ResultMessage(500, "아이템 삭제 중 오류가 발생했습니다.");
        }
    }

    // 고유 값으로 특정 상품 조회
    @Override
    public Item findByUniqueValue(String uniqueValue) {
        return itemRepository.findByUniqueValue(uniqueValue).orElse(null);
    }

    // 모든 상품 조회
    @Override
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }
}