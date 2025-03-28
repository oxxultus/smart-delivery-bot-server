package bwajo.bwajoserver.controller;

import bwajo.bwajoserver.dto.BodyItem;
import bwajo.bwajoserver.entity.Item;
import bwajo.bwajoserver.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ProductController {

    private final ItemService itemService;

    @Autowired
    public ProductController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/add-product")
    public String addProduct() {
        return "addproduct";
    }

    @GetMapping("/list-product")
    public String listProduct(Model model) {
        List<Item> items = itemService.findAllItems();
        model.addAttribute("items", items);
        return "listproduct";
    }

    @PostMapping("/addItem")
    public String addItem(@RequestBody BodyItem bodyItem) {
        itemService.addItem(bodyItem.getName(), bodyItem.getPrice(), bodyItem.getStockQuantity(), bodyItem.getCategory(), bodyItem.getUniqueValue());
        return "redirect:/add-product";
    }
}
