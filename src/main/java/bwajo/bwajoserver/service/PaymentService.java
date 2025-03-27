package bwajo.bwajoserver.service;

import bwajo.bwajoserver.entity.CartList;
import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.User;

import java.util.List;

public interface PaymentService {

    // 장바구니에서 결제내역으로 옴기기 - 로봇작업시작
    String addPayment(User user, CartList cartList);

    // 결제 취소
    String undoPayment(User user, CartList cartList);

    // 사용자의 결제 내역
    List<PaymentList> findPaymentsForUser(User user);

    // 모든 사용자의 결제 내역
    List<PaymentList> findAllPaymentsList();
}
