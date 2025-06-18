package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.model.Delivery;

@Mapper
public abstract class DeliveryMapper {

    public static Delivery toDelivery(DeliveryDto dto) {
        Delivery delivery = new Delivery();
        if (dto.getDeliveryId() != null) {
            delivery.setDeliveryId(dto.getDeliveryId());
            delivery.setDeliveryState(dto.getDeliveryState());
        } else
            delivery.setDeliveryState(DeliveryState.CREATED);
        delivery.setOrderId(dto.getOrderId());
        AddressDto address = dto.getToAddress();
        delivery.setToCountry(address.getCountry());
        delivery.setToCity(address.getCity());
        delivery.setToStreet(address.getStreet());
        delivery.setToHouse(address.getHouse());
        delivery.setToFlat(address.getFlat());
        address = dto.getFromAddress();
        delivery.setFromCountry(address.getCountry());
        delivery.setFromCity(address.getCity());
        delivery.setFromStreet(address.getStreet());
        delivery.setFromHouse(address.getHouse());
        delivery.setFromFlat(address.getFlat());

        return delivery;
    }

    public static DeliveryDto toDto(Delivery delivery) {
        DeliveryDto dto = new DeliveryDto();
        dto.setDeliveryId(delivery.getDeliveryId());
        AddressDto toAddress = new AddressDto();
        toAddress.setCountry(delivery.getToCountry());
        toAddress.setCity(delivery.getToCity());
        toAddress.setStreet(delivery.getToStreet());
        toAddress.setHouse(delivery.getToHouse());
        toAddress.setFlat(delivery.getToFlat());
        dto.setToAddress(toAddress);
        AddressDto fromAddress = new AddressDto();
        fromAddress.setCountry(delivery.getFromCountry());
        fromAddress.setCity(delivery.getFromCity());
        fromAddress.setStreet(delivery.getFromStreet());
        fromAddress.setHouse(delivery.getFromHouse());
        fromAddress.setFlat(delivery.getFromFlat());
        dto.setFromAddress(fromAddress);
        dto.setOrderId(delivery.getOrderId());
        dto.setDeliveryState(delivery.getDeliveryState());
        return dto;
    }
}
