package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.circuit.WarehouseFallback;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.requests.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallback = WarehouseFallback.class)
public interface WarehouseClient {

    @PostMapping("/check")
    BookedProductsDto checkQuantity(@RequestBody ShoppingCartDto cart);

    @PutMapping
    void addProduct(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/add")
    void incQuantity(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductForOrderFromShoppingCart(@RequestBody AssemblyProductsForOrderRequest request);

    @PostMapping("/shipped")
    void setShipped(@RequestBody ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, Integer> products);
}
