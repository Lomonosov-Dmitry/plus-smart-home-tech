package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.enums.PaymentStatus;
import ru.yandex.practicum.exceptions.NoOrderFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mappers.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Autowired
    private final ShoppingStoreClient shoppingStoreClient;

    @Autowired
    private final PaymentRepository repository;

    @Autowired
    private final OrderClient orderClient;

    public Double productCost(OrderDto dto) {
        double productCost = 0.0;
        for (UUID productId : dto.getProducts().keySet()) {
            productCost += shoppingStoreClient.getProductById(productId).getPrice() * dto.getProducts().get(productId);
        }
        return productCost;
    }

    public Double getTotalCost(OrderDto dto) {
        return dto.getProductPrice() * 1.1 + dto.getDeliveryPrice();
    }

    public PaymentDto payment(OrderDto dto) {
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setState(PaymentStatus.PENDING);
        payment.setTotalPayment(dto.getTotalPrice());
        payment.setDeliveryTotal(dto.getDeliveryPrice());
        payment.setFeeTotal(0.0);
        return PaymentMapper.INSTANCE.toDto(repository.save(payment));
    }

    //Не забыть добавить обновление статуса заказа
    public void paymentSuccess(UUID paymentId) {
        Payment payment = repository.findById(paymentId).orElseThrow(() ->
                new NoOrderFoundException("Не найдена оплата с ID = " + paymentId));
        payment.setState(PaymentStatus.SUCCESS);
        orderClient.paymentSuccess(payment.getOrderId());
        repository.save(payment);
    }

    //Не забыть добавить обновление статуса заказа
    public void paymentFailed(UUID paymentId) {
        Payment payment = repository.findById(paymentId).orElseThrow(() ->
                new NoOrderFoundException("Не найдена оплата с ID = " + paymentId));
        payment.setState(PaymentStatus.FAILED);
        orderClient.paymentFailed(payment.getOrderId());
        repository.save(payment);
    }
}
