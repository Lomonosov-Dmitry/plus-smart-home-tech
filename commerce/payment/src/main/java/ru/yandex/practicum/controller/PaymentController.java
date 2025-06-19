package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private final PaymentService service;

    @PostMapping
    public PaymentDto payment(@RequestBody OrderDto dto) {
        return service.payment(dto);
    }

    @PostMapping("/productCost")
    public Double productCost(@RequestBody OrderDto dto) {
        return service.productCost(dto);
    }

    @PostMapping("/totalCost")
    public Double getTotalCost(@RequestBody OrderDto dto) {
        return service.getTotalCost(dto);
    }

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void paymentSuccess(@RequestBody UUID paymentId) {
        service.paymentSuccess(paymentId);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void paymentFailed(@RequestBody UUID paymentId) {
        service.paymentFailed(paymentId);
    }
}
