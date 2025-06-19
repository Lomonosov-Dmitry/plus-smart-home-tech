package ru.yandex.practicum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.circuit.PaymentFallback;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment", fallback = PaymentFallback.class)
public interface PaymentClient {

    @PostMapping
    PaymentDto payment(@RequestBody OrderDto dto);

    @PostMapping("/productCost")
    Double productCost(@RequestBody OrderDto dto);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody OrderDto dto);

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    void paymentSuccess(@RequestBody UUID paymentId);

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    void paymentFailed(@RequestBody UUID paymentId);
}
