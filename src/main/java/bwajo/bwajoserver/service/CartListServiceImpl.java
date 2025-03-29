package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.CartItem;
import bwajo.bwajoserver.entity.CartList;
import bwajo.bwajoserver.entity.Item;
import bwajo.bwajoserver.entity.User;
import bwajo.bwajoserver.repository.CartItemRepository;
import bwajo.bwajoserver.repository.CartListRepository;
import bwajo.bwajoserver.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartListServiceImpl implements CartListService {

    private final CartItemRepository cartItemRepository;
    private final CartListRepository cartListRepository;
    private final PaymentService paymentService;
    private final ItemRepository itemRepository;

    @Autowired
    public CartListServiceImpl(CartItemRepository cartItemRepository, CartListRepository cartListRepository, PaymentService paymentService, ItemRepository itemRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartListRepository = cartListRepository;
        this.paymentService = paymentService;
        this.itemRepository = itemRepository;
    }

    // 장바구니에 아이템 추가
    @Override
    public ResultMessage addItem(User user, Item item, int quantity, Long totalPrice) {
        // 1. 해당 사용자가 이미 장바구니를 갖고 있는지 확인
        CartList cartList = cartListRepository.findByUser(user);

        // 2. 장바구니가 없으면 새로 생성
        if (cartList == null) {
            cartList = new CartList();
            cartList.setUser(user);
        }

        // 3. 새로운 CartItem 생성
        CartItem cartItem = new CartItem();
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(totalPrice);

        // 4. CartList에 CartItem 추가
        cartList.addCartItem(cartItem);

        // 5. 장바구니 업데이트 (새로 생성한 장바구니를 저장)
        cartListRepository.save(cartList);

        return new ResultMessage(201, "아이템이 장바구니에 추가되었습니다.");
    }

    // 장바구니에서 아이템 삭제
    @Override
    public ResultMessage deleteItem(User user, String uniqueValue) {
        // 1. uniqueValue에 해당하는 Item을 찾기
        Optional<Item> itemOp = itemRepository.findByUniqueValue(uniqueValue);
        if (itemOp.isEmpty()) {
            return new ResultMessage(404, "해당 아이템을 찾을 수 없습니다.");
        }
        Item item = itemOp.get();

        // 2. 해당 사용자가 장바구니를 갖고 있는지 확인
        CartList cartList = cartListRepository.findByUser(user);
        if (cartList == null) {
            return new ResultMessage(404, "장바구니를 찾을 수 없습니다.");
        }

        // 3. 해당 장바구니에서 삭제할 아이템 찾기
        CartItem cartItemToDelete = null;
        for (CartItem cartItem : cartList.getCartItems()) {
            if (cartItem.getItem().equals(item)) {
                cartItemToDelete = cartItem;
                break;
            }
        }

        if (cartItemToDelete == null) {
            return new ResultMessage(404, "해당 아이템이 장바구니에 없습니다.");
        }

        // 4. 장바구니에서 아이템 삭제
        cartList.getCartItems().remove(cartItemToDelete);
        cartItemRepository.delete(cartItemToDelete); // 실제로 아이템 삭제

        // 5. 변경된 장바구니 저장
        cartListRepository.save(cartList);

        return new ResultMessage(200, "아이템이 장바구니에서 삭제되었습니다.");
    }

    // 장바구니에서 아이디로 아이템 삭제
    @Override
    public ResultMessage deleteItemById(User user, Long id) {
        // 1. uniqueValue에 해당하는 Item을 찾기
        Optional<CartItem> cartItemOp = cartItemRepository.findById(id);

        System.out.println("ID:" + id);

        if (cartItemOp.isEmpty()) {
            return new ResultMessage(404, "해당 아이템을 찾을 수 없습니다.");
        }

        CartItem cartItem = cartItemOp.get();

        // 2. 해당 사용자가 장바구니를 갖고 있는지 확인
        CartList cartList = cartListRepository.findByUser(user);
        if (cartList == null) {
            System.out.println("\"장바구니를 찾을 수 없습니다.\"");
            return new ResultMessage(404, "장바구니를 찾을 수 없습니다.");
        }

        // 3. 해당 장바구니에서 삭제할 아이템 찾기
        CartItem cartItemToDelete = null;
        for (CartItem checkCartItem : cartList.getCartItems()) {
            if (checkCartItem.getId().equals(cartItem.getId())) {
                cartItemToDelete = cartItem;
                break;
            }
        }

        if (cartItemToDelete == null) {
            System.out.println("\"해당 아이템이 장바구니에 없습니다..\"");
            return new ResultMessage(404, "해당 아이템이 장바구니에 없습니다.");
        }

        // 4. 장바구니에서 아이템 삭제
        cartList.getCartItems().remove(cartItemToDelete);
        cartItemRepository.delete(cartItemToDelete); // 실제로 아이템 삭제

        // 5. 변경된 장바구니 저장
        cartListRepository.save(cartList);

        System.out.println("\"아이템이 장바구니에서 삭제되었습니다.\"");
        return new ResultMessage(200, "아이템이 장바구니에서 삭제되었습니다.");
    }

    @Override
    public CartList getCartItems(User user) {
        return cartListRepository.findByUser(user);
    }

    // 결제 처리
    @Override
    public ResultMessage payment(User user) {
        CartList cartList = cartListRepository.findByUser(user);
        if (cartList == null || cartList.getCartItems().isEmpty()) {
            return new ResultMessage(404, "장바구니에 아이템이 없습니다.");
        }

        if (paymentService.addPayment(user, cartList).getCode() == 201) {
            // 장바구니 아이템 삭제
            cartList.getCartItems().clear();
            cartListRepository.save(cartList);  // CartList 저장
        } else {
            return new ResultMessage(400, "결제 실패. 장바구니가 유지됩니다.");
        }

        return new ResultMessage(200, "결제 성공. 장바구니가 비워졌습니다.");
    }
}