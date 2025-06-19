package ru.yandex.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.requests.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.requests.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final OrderService service;

    @PutMapping
    public OrderDto newOrder(@RequestBody CreateNewOrderRequest request) {
        return service.newOrder(request);
    }

    @PostMapping("/return")
    public OrderDto returnOrder(@RequestBody ProductReturnRequest request) {
        return service.returnOrder(request);
    }

    @PostMapping("/payment")
    public OrderDto payment(@RequestBody UUID orderId) {
        return service.payment(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        return service.paymentFailed(orderId);
    }

    @PostMapping("/payment/success")
    public OrderDto paymentSuccess(@RequestBody UUID orderId) {
        return service.paymentSuccess(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto delivery(@RequestBody UUID orderId) {
        return service.delivery(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {
        return service.deliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completed(@RequestBody UUID orderId) {
        return service.completed(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody UUID orderId) {
        return service.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody UUID orderId) {
        return service.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestBody UUID orderId) {
        return service.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {
        return service.assemblyFailed(orderId);
    }
}
