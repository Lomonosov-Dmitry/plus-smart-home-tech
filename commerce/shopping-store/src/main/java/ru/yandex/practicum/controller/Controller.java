package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/shopping-store")
@RequiredArgsConstructor
public class Controller {
    private final ShoppingStoreService service;

    @PutMapping
    public ProductDto newProduct(@RequestBody ProductDto productDto) {
        return service.newProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean removeProduct(@RequestBody UUID id) {
        return service.removeProduct(id);
    }

    @PostMapping("/quantityState")
    public ProductDto updateProductQuantity(@RequestParam UUID productId,
                                            @RequestParam String quantityState) {
        return service.updateProductQuantity(productId, quantityState);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam String category,
                                           @RequestParam(defaultValue = "0", required = false) Integer page,
                                           @RequestParam(defaultValue = "10", required = false) Integer size,
                                           @RequestParam(required = false) List<String> sort) {
        return service.getAllProducts(category, page, size, sort);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable UUID productId) {
        return service.getProductById(productId);
    }
}
