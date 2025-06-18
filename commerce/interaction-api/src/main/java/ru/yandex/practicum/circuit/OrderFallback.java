package ru.yandex.practicum.circuit;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.exceptions.Unavalible;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.requests.CreateNewOrderRequest;
import ru.yandex.practicum.requests.ProductReturnRequest;

import java.util.UUID;

public class OrderFallback implements OrderClient {

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto completed(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto paymentSuccess(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto payment(UUID orderId) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequest request) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public OrderDto newOrder(CreateNewOrderRequest request) {
        throw new Unavalible("Сервис временно недоступен");
    }
}
