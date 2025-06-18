package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    @Autowired
    private final DeliveryService service;

    @PutMapping
    public DeliveryDto planDelivery(@RequestBody DeliveryDto dto) {
        return service.planDelivery(dto);
    }

    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    public void delivery(@RequestBody UUID deliveryId) {
        service.delivery(deliveryId);
    }

    @PostMapping("/picked")
    @ResponseStatus(HttpStatus.OK)
    public void deliveryPicked(@RequestBody UUID deliveryId) {
        service.deliveryPicked(deliveryId);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void deliveryFailed(@RequestBody UUID deliveryId) {
        service.deliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    public Double deliveryCost(@RequestBody OrderDto dto) {
        return service.deliveryCost(dto);
    }
}
