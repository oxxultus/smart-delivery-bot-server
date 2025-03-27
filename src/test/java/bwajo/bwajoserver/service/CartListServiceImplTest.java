package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.*;
import bwajo.bwajoserver.repository.CartItemRepository;
import bwajo.bwajoserver.repository.CartListRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CartListServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartListRepository cartListRepository;

    @InjectMocks
    private CartListServiceImpl cartListService;

    @Test
    public void testAddItemToCart_Success() {
        User user = new User();
        Item item = new Item();
        int quantity = 1;
        Long totalPrice = 1000L;

        CartList cartList = new CartList();
        cartList.setUser(user);

        Mockito.when(cartListRepository.findByUser(user)).thenReturn(null); // 장바구니 없음
        Mockito.when(cartListRepository.save(Mockito.any(CartList.class))).thenReturn(cartList);

        ResultMessage result = cartListService.addItem(user, item, quantity, totalPrice);

        assertEquals(201, result.getCode());
        assertEquals("아이템이 장바구니에 추가되었습니다.", result.getMessage());
    }

    @Test
    public void testDeleteItemFromCart_Success() {
        User user = new User();
        Item item = new Item();

        CartList cartList = new CartList();
        cartList.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartList.addCartItem(cartItem);

        Mockito.when(cartListRepository.findByUser(user)).thenReturn(cartList);

        ResultMessage result = cartListService.deleteItem(user, item);

        assertEquals(200, result.getCode());
        assertEquals("아이템이 장바구니에서 삭제되었습니다.", result.getMessage());
    }

    // 추가적인 테스트 메서드들 작성
}