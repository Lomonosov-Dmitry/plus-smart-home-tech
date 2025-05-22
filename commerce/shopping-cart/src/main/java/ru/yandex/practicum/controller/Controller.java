package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProdInCartDto;
import ru.yandex.practicum.model.Cart;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class Controller {

    @GetMapping
    public Cart getCart(@RequestParam String username) {
        return null;
    }

    @PutMapping
    public Cart addProducts(@RequestParam String username, @RequestBody ProdInCartDto dto) {
        return null;
    }

    @DeleteMapping
    public void deleteCart(@RequestParam String username) {

    }

    @PostMapping("/remove")
    public Cart removeProducts(@RequestParam String username, @RequestBody ProdInCartDto dto) {
        return null;
    }

    @PostMapping("/change-quantity")
    public Cart changeQuantity(@RequestParam String username, @RequestBody ProdInCartDto dto) {
        return null;
    }
}
