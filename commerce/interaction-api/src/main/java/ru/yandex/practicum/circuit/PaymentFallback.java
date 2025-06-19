package ru.yandex.practicum.circuit;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.exceptions.Unavalible;
import ru.yandex.practicum.feign.PaymentClient;

import java.util.UUID;

public class PaymentFallback implements PaymentClient {
    @Override
    public PaymentDto payment(OrderDto dto) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public Double productCost(OrderDto dto) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public Double getTotalCost(OrderDto dto) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        throw new Unavalible("Сервис временно недоступен");
    }
}
