package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.dto.BodyItem;
import bwajo.bwajoserver.dto.ResultMessage;
import bwajo.bwajoserver.entity.Item;
import bwajo.bwajoserver.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    // 의존성을 주입하기 위한 부분 ⬇ ==================================================

    private final ItemService itemService;

    @Autowired
    public ProductController(ItemService itemService) {
        this.itemService = itemService;
    }

    // HTML을 제공하기 위한 부분 ⬇ ==================================================

    @GetMapping("/products")
    public String listProduct(Model model) {
        List<Item> items = itemService.findAllItems();
        model.addAttribute("items", items);
        return "products";
    }

    // 데이터를 받아와서 처리하기 위한 부분 ⬇ ==================================================

    @PostMapping("/addItem")
    public String addItem(@RequestBody BodyItem bodyItem) {
        itemService.addItem(bodyItem.getName(), bodyItem.getPrice(), bodyItem.getStockQuantity(), bodyItem.getCategory(), bodyItem.getUniqueValue());
        return "products";
    }

    @PostMapping("/deleteItem")
    public ResponseEntity<Map<String, Object>> deleteItem(@RequestBody BodyItem bodyItem) {
        Map<String, Object> response = new HashMap<>();

        // 서비스에서 반환된 결과를 받기
        ResultMessage resultMessage = itemService.deleteItem(bodyItem.getUniqueValue());

        // 결과에 따른 응답 처리
        if (resultMessage.getCode() == 200) {
            response.put("success", true);
            response.put("message", resultMessage.getMessage());
            return ResponseEntity.ok(response);  // 성공적인 삭제 응답 반환
        } else {
            response.put("success", false);
            response.put("message", resultMessage.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 실패 응답 반환
        }
    }
}
