package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @PutMapping
    ProductDto newProduct(@RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(@RequestBody UUID id);

    @PostMapping("/quantityState")
    ProductDto updateProductQuantity(@RequestParam UUID productId,
                                     @RequestParam String quantityState);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @GetMapping
    List<ProductDto> getAllProducts(@RequestParam String category,
                                    @RequestParam(defaultValue = "0", required = false) Integer page,
                                    @RequestParam(defaultValue = "10", required = false) Integer size,
                                    @RequestParam(required = false) List<String> sort);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);
}
