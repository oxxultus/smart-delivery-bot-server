package bwajo.bwajoserver.service;

import bwajo.bwajoserver.dto.PaymentListNumber;
import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.CartList;
import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.PaymentStatus;
import bwajo.bwajoserver.entity.User;

import java.util.List;

public interface PaymentService {

    // 장바구니에서 결제내역으로 옴기기 - 로봇작업시작
    ResultMessage addPayment(User user, CartList cartList);

    // 결제 취소
    ResultMessage undoPayment(User user, String paymentListUniqueNumber);

    // 사용자의 결제 내역
    List<PaymentList> findPaymentsForUser(User user);

    // 결재내역 상태 변경
    PaymentListNumber updateStatus(String paymentListUniqueNumber, PaymentStatus paymentStatus);

    // 모든 사용자의 결제 내역
    List<PaymentList> findAllPaymentsList();
}
