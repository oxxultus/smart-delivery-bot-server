package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.entity.PaymentList;
import bwajo.bwajoserver.entity.User;
import bwajo.bwajoserver.service.CartListService;
import bwajo.bwajoserver.service.PaymentService;
import bwajo.bwajoserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
}
