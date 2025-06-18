package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.requests.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    private WarehouseService service;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody NewProductInWarehouseRequest request) {
        service.addProduct(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkQuantity(@RequestBody ShoppingCartDto cart) {
        return service.checkCart(cart);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void incQuantity(@RequestBody AddProductToWarehouseRequest request) {
        service.incProductQuantity(request);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        return service.getAddress();
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(@RequestBody AssemblyProductsForOrderRequest request) {
        return service.assembly(request);
    }

    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.OK)
    public void setShipped(@RequestBody ShippedToDeliveryRequest request) {
        service.setShipped(request);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public void returnProducts(@RequestBody Map<UUID, Integer> products) {
        service.returnProducts(products);
    }
}
