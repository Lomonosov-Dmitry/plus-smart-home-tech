package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mappers.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.requests.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.requests.CreateNewOrderRequest;
import ru.yandex.practicum.repository.OrdersRepository;
import ru.yandex.practicum.requests.ProductReturnRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrdersRepository repository;
    @Autowired
    private final DeliveryClient deliveryClient;
    @Autowired
    private final PaymentClient paymentClient;
    @Autowired
    private final WarehouseClient warehouseClient;

    @Transactional
    public OrderDto newOrder(CreateNewOrderRequest request) {
        BookedProductsDto bookedProductsDto =
                warehouseClient.checkQuantity(ShoppingCartDto.of(request.getShoppingCart().getCartId(),
                        request.getShoppingCart().getProducts()));
        Order order = new Order();
        order.setShoppingCartId(request.getShoppingCart().getCartId());
        order.setProducts(request.getShoppingCart().getProducts());
        order.setState(OrderState.NEW);
        order.setFragile(bookedProductsDto.getFragile());
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryState(DeliveryState.CREATED);
        deliveryDto.setOrderId(repository.save(order).getOrderId());
        deliveryDto.setToAddress(request.getDeliveryAddress());
        deliveryDto.setFromAddress(warehouseClient.getWarehouseAddress());
        order.setDeliveryId(deliveryClient.planDelivery(deliveryDto).getDeliveryId());
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto returnOrder(ProductReturnRequest request) {
        Order order = repository.findById(request.getOrderId()).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + request.getOrderId()));
        order.setState(OrderState.PRODUCT_RETURNED);
        warehouseClient.returnProducts(request.getProducts());
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto payment(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        PaymentDto payment = paymentClient.payment(OrderMapper.toDto(order));
        order.setPaymentId(payment.getPaymentId());
        order.setTotalPrice(payment.getTotalPayment());
        order.setDeliveryPrice(payment.getDeliveryTotal());
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto paymentFailed(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setState(OrderState.PAYMENT_FAILED);
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto paymentSuccess(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setState(OrderState.PAID);
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto delivery(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setState(OrderState.DELIVERED);
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto deliveryFailed(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setState(OrderState.DELIVERY_FAILED);
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto completed(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setState(OrderState.COMPLETED);
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto calculateTotal(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setTotalPrice(paymentClient.getTotalCost(OrderMapper.toDto(order)));
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto calculateDelivery(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setDeliveryPrice(deliveryClient.deliveryCost(OrderMapper.toDto(order)));
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto assembly(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest();
        request.setProducts(order.getProducts());
        request.setOrderId(orderId);
        BookedProductsDto bookedProductsDto = warehouseClient.assemblyProductForOrderFromShoppingCart(request);
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        order.setFragile(bookedProductsDto.getFragile());
        return OrderMapper.toDto(repository.save(order));
    }

    public OrderDto assemblyFailed(UUID orderId) {
        Order order = repository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Не найден заказ с ID = " + orderId));
        order.setState(OrderState.ASSEMBLY_FAILED);
        return OrderMapper.toDto(repository.save(order));
    }
}
