package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;
import org.springframework.context.annotation.ComponentScan;
import bwajo.bwajoserver.entity.*;
import bwajo.bwajoserver.service.*;

@SpringBootTest
@ComponentScan(basePackages = "bwajo.bwajoserver.service") // 패키지 추가
public class ShoppingCartTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartListService cartListService;

    @Autowired
    private PaymentService paymentService;

    @Test
    @Transactional
    @Rollback(true)  // DB에 저장되지 않도록 롤백 처리
    public void testPaymentProcessing() {
        // 1. 아이템 생성
        String itemName = "아이템1";
        Long price = 10000L;
        Long stockQuantity = 50L;
        String category = "전자제품";
        String uniqueValue = "unique_item_001";

        ResultMessage itemResult = itemService.addItem(itemName, price, stockQuantity, category, uniqueValue);
        System.out.println(itemResult.getMessage());

        // 2. 사용자 생성
        String email = "user@example.com";
        String password = "password123";
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(Role.USER);
        ResultMessage userResult = userService.register(newUser);
        System.out.println(userResult.getMessage());

        // 3. 아이템을 장바구니에 추가
        Item item = new Item();
        item.setUniqueValue(uniqueValue);
        item.setName(itemName);
        item.setPrice(price);
        CartList cartList = new CartList();
        cartList.setUser(newUser);
        cartListService.addItem(newUser, item, 2, price * 2); // 수량 2, 총 가격 20000

        // 4. 결제 처리
        ResultMessage paymentResult = cartListService.payment(newUser);
        System.out.println(paymentResult.getMessage());
    }
}