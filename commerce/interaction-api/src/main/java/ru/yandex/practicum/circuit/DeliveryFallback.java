package ru.yandex.practicum.circuit;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.exceptions.Unavalible;
import ru.yandex.practicum.feign.DeliveryClient;

import java.util.UUID;

public class DeliveryFallback implements DeliveryClient {
    @Override
    public DeliveryDto planDelivery(DeliveryDto dto) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void delivery(UUID deliveryId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void deliveryPicked(UUID deliveryId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void deliveryFailed(UUID deliveryId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public Double deliveryCost(OrderDto dto) {
        throw new Unavalible("Сервис временно недоступен");
    }
}
