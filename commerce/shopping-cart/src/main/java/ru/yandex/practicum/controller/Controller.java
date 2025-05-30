package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    private final CartService service;

    @GetMapping
    public ShoppingCartDto getCart(@RequestParam String username) {
        return service.getUserCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProducts(@RequestParam String username, @RequestBody Map<UUID, Integer> cart) {
        return service.addProduct(username, cart);
    }

    @DeleteMapping
    public void deleteCart(@RequestParam String username) {
        service.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeProducts(@RequestParam String username, @RequestBody List<String> prods) {
        return service.removeProducts(username, prods);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeQuantity(@RequestParam String username, @RequestBody ChangeProductQuantityRequest request) {
        return service.changeQuantity(username, request);
    }
}
