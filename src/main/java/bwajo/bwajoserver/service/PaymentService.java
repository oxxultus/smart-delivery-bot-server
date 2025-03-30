package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.PaymentListNumberResultMessage;
import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.CartList;
import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.PaymentStatus;
import bwajo.bwajoserver.entity.User;

import java.util.List;

public interface PaymentService {

    // 결제 추가
    PaymentListNumberResultMessage addPayment(User user, CartList cartList);

    // 결제 취소
    ResultMessage undoPayment(User user, String paymentListUniqueNumber);

    // 사용자의 모든 결제 내역
    List<PaymentList> findPaymentsForUser(User user);

    // 상품 상태 변경
    PaymentListNumberResultMessage updateStatus(String paymentListUniqueNumber, PaymentStatus paymentStatus);

    // 모든 사용자의 결제 내역
    List<PaymentList> findAllPaymentsList();
}
