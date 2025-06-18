package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.model.Order;


@Mapper
public abstract class OrderMapper {

    public static OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setFragile(order.getFragile());
        dto.setDeliveryWeight(order.getDeliveryWeight());
        dto.setDeliveryVolume(order.getDeliveryVolume());
        dto.setDeliveryId(order.getDeliveryId());
        dto.setDeliveryPrice(order.getDeliveryPrice());
        dto.setPaymentId(order.getPaymentId());
        dto.setProducts(order.getProducts());
        dto.setState(order.getState());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setProductPrice(order.getProductPrice());
        dto.setShoppingCartId(order.getShoppingCartId());
        return dto;
    }

}
