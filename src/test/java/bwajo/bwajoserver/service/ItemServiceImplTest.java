package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.Item;
import bwajo.bwajoserver.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    public void testAddItem_Success() {
        String name = "Test Item";
        Long price = 1000L;
        Long stockQuantity = 50L;
        String category = "Electronics";
        String uniqueValue = "unique-123";

        // Mocking the behavior of itemRepository
        Mockito.when(itemRepository.findByUniqueValue(uniqueValue)).thenReturn(Optional.empty());

        ResultMessage result = itemService.addItem(name, price, stockQuantity, category, uniqueValue);

        assertEquals(201, result.getCode());
        assertEquals("아이템이 추가되었습니다.", result.getMessage());
    }

    @Test
    public void testAddItem_ItemExists() {
        String name = "Test Item";
        Long price = 1000L;
        Long stockQuantity = 50L;
        String category = "Electronics";
        String uniqueValue = "unique-123";

        // Mocking the behavior of itemRepository to simulate existing item
        Mockito.when(itemRepository.findByUniqueValue(uniqueValue)).thenReturn(Optional.of(new Item()));

        ResultMessage result = itemService.addItem(name, price, stockQuantity, category, uniqueValue);

        assertEquals(201, result.getCode());
        assertEquals("아이템이 추가되었습니다.", result.getMessage());
    }

    // 추가적인 테스트 메서드들 작성
}