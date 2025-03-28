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

    UserService userService;
    PaymentService paymentService;
    CartListService cartListService;

    @Autowired
    public PaymentController(UserService userService, PaymentService paymentService, CartListService cartListService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.cartListService = cartListService;
    }

    @GetMapping("/payment-result")
    public String paymentResult(Model model) {
        User user = userService.getUserByEmail("관리자");
        List<PaymentList> paymentLists = paymentService.findPaymentsForUser(user);
        model.addAttribute("paymentLists", paymentLists);
        return "paymentresult";
    }

    @PostMapping("/payment")
    public String payment(){
        User user = userService.getUserByEmail("관리자");
        cartListService.payment(user);

        return "redirect:/payment-result";
    }

    @PostMapping("/deletePayment")
    public ResponseEntity<Map<String, Object>> deletePayment(@RequestBody Map<String, String> request) {
        String paymentUniqueNumber = request.get("paymentUniqueNumber");
        Map<String, Object> response = new HashMap<>();

        try {
            // 결제 삭제 로직
            User user = userService.getUserByEmail("관리자");  // 관리자 정보 가져오기
            paymentService.undoPayment(user, paymentUniqueNumber);  // 결제 취소 처리

            response.put("success", true);
            response.put("message", "결제가 삭제되었습니다.");
            return ResponseEntity.ok(response);  // 성공적인 삭제 응답 반환
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "결제 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // 내부 서버 오류 응답
        }
    }
}
