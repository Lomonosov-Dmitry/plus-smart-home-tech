package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.circuit.WarehouseFallback;
import ru.yandex.practicum.dto.*;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseFallback.class)
public interface WarehouseClient {

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody ShoppingCartDto cart);

    @PutMapping
    void addProduct(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void incQuantity(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}
