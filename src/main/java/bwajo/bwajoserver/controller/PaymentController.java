package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.User;
import bwajo.bwajoserver.service.CartListService;
import bwajo.bwajoserver.service.PaymentService;
import bwajo.bwajoserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {

    // 의존성을 주입하기 위한 부분 ⬇ ==================================================

    UserService userService;
    PaymentService paymentService;
    CartListService cartListService;

    @Autowired
    public PaymentController(UserService userService, PaymentService paymentService, CartListService cartListService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.cartListService = cartListService;
    }

    // HTML을 제공하기 위한 부분 ⬇ ==================================================

    @GetMapping("/payment-result")
    public String paymentResult(Model model) {
        User user = userService.getUserByEmail("관리자");
        List<PaymentList> paymentLists = paymentService.findPaymentsForUser(user);
        model.addAttribute("paymentLists", paymentLists);
        return "paymentresult";
    }

    // 데이터를 받아와서 처리하기 위한 부분 ⬇ ==================================================

    @PostMapping("/payment")
    public String payment(){
        User user = userService.getUserByEmail("관리자");
        cartListService.payment(user);

        return "redirect:/payment-result";
    }

    @PostMapping("/deletePayment")
    public ResponseEntity<ResultMessage> deletePayment(@RequestBody Map<String, String> request) {
        String paymentUniqueNumber = request.get("paymentUniqueNumber");

        try {
            // 결제 삭제 로직
            User user = userService.getUserByEmail("관리자");  // 관리자 정보 가져오기
            ResultMessage result = paymentService.undoPayment(user, paymentUniqueNumber);  // 결제 취소 처리

            return ResponseEntity.status(result.getCode()).body(result); // HTTP 상태 코드와 메시지 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResultMessage(500, "결제 삭제 중 오류가 발생했습니다."));  // 내부 서버 오류 응답
        }
    }
}
