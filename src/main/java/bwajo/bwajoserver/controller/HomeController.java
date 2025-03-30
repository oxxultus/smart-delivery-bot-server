package bwajo.bwajoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // HTML을 제공하기 위한 부분 ⬇ ==================================================

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
