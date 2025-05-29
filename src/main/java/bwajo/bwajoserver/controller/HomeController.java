package bwajo.bwajoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // HTML을 제공하기 위한 부분 ⬇ ==================================================

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/test")
    public String showControlPage(Model model) {
        model.addAttribute("robotBaseUrl", "http://oxxultus-bot.kro.kr:8081");
        return "test"; // robot-control.html
    }

}
