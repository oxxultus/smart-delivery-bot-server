package bwajo.bwajoserver.controller;


import bwajo.bwajoserver.dto.BodyItem;
import bwajo.bwajoserver.dto.DeleteCartItemRequest;
import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.*;
import bwajo.bwajoserver.service.CartListService;
import bwajo.bwajoserver.service.ItemService;
import bwajo.bwajoserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Controller
public class CartController {

    CartListService cartListService;
    ItemService itemService;
    UserService userService;

    @Autowired
    public CartController(CartListService cartListService, ItemService itemService, UserService userService) {
        this.cartListService = cartListService;
        this.itemService = itemService;
        this.userService = userService;
    }

    public CartController(CartListService cartListService, ItemService itemService) {
        this.cartListService = cartListService;
        this.itemService = itemService;
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        User user = userService.getUserByEmail("관리자");
        CartList cartList = cartListService.getCartItems(user);
        List<CartItem> cartItems = cartList.getCartItems();
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/addCart")
    public String addCart(@RequestBody BodyItem bodyItem) {
        // 유니크 값으로 아이템 조회 (없는 경우 예외 처리)
        Item item = itemService.findByUniqueValue(bodyItem.getUniqueValue());
        if (item == null) {
            return "redirect:/error?message=ItemNotFound"; // 에러 페이지 또는 메시지 전달
        }

        // 관리자 계정 조회 (없는 경우 예외 처리)
        User user = userService.getUserByEmail("관리자");
        if (user == null) {
            return "redirect:/error?message=UserNotFound"; // 에러 페이지로 이동
        }

        // 장바구니에 아이템 추가
        cartListService.addItem(user, item, 1, bodyItem.getPrice());

        // 장바구니 페이지로 이동
        return "redirect:/products"; // 또는 원래 페이지로 리디렉션
    }

    @PostMapping("/deleteCartItem")
    public String deleteCartItem(@RequestBody DeleteCartItemRequest request) {
        // "관리자" 이메일로 사용자 정보 가져오기
        User user = userService.getUserByEmail("관리자");

        // 아이템 삭제
        ResultMessage result = cartListService.deleteItemById(user, request.getCartItemId());

        // 삭제 결과 반환
        return "redirect:/cart";
    }
}
