package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.exceptions.NoDeliveryFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mappers.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    @Autowired
    private final DeliveryRepository repository;
    @Autowired
    private final WarehouseClient warehouseClient;
    @Autowired
    private final OrderClient orderClient;

    public DeliveryDto planDelivery(DeliveryDto dto) {
        return DeliveryMapper.toDto(repository.save(DeliveryMapper.toDelivery(dto)));
    }

    public void delivery(UUID deliveryId) {
        Delivery delivery = repository.findById(deliveryId).orElseThrow(() ->
                new NoDeliveryFoundException("Не найдена доставка с ID = " + deliveryId));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.delivery(delivery.getOrderId());
        repository.save(delivery);
    }

    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = repository.findById(deliveryId).orElseThrow(() ->
                new NoDeliveryFoundException("Не найдена доставка с ID = " + deliveryId));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        warehouseClient.setShipped(ShippedToDeliveryRequest.of(delivery.getOrderId(), deliveryId));
        repository.save(delivery);
    }

    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = repository.findById(deliveryId).orElseThrow(() ->
                new NoDeliveryFoundException("Не найдена доставка с ID = " + deliveryId));
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.deliveryFailed(delivery.getOrderId());
        repository.save(delivery);
    }

    public Double deliveryCost(OrderDto dto) {
        Delivery delivery = repository.findById(dto.getDeliveryId()).orElseThrow(() ->
                new NoDeliveryFoundException("Не найдена доставка с ID = " + dto.getDeliveryId()));
        double totalCost = 5.0;
        if (warehouseClient.getWarehouseAddress().contains("ADDRESS_2"))
            totalCost = 15;
        if (dto.getFragile())
            totalCost = totalCost * 1.2;
        delivery.setFragile(dto.getFragile());
        totalCost += dto.getDeliveryWeight() * 0.3;
        delivery.setDeliveryWeight(dto.getDeliveryWeight());
        totalCost += dto.getDeliveryVolume() * 0.2;
        delivery.setDeliveryVolume(dto.getDeliveryVolume());
        if (!delivery.getFromStreet().equals(warehouseClient.getWarehouseAddress().getStreet()))
            totalCost = totalCost * 1.2;
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        repository.save(delivery);
        return totalCost;
    }
}
