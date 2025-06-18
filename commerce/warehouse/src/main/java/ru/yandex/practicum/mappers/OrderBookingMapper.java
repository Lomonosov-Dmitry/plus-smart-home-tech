package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.model.OrderBooking;

import java.util.UUID;

@Mapper
public abstract class OrderBookingMapper {
    public static BookedProductsDto toBookedProducts(OrderBooking booking) {
        BookedProductsDto dto = new BookedProductsDto();
        dto.setFragile(booking.getFragile());
        dto.setDeliveryVolume(booking.getDeliveryVolume());
        dto.setDeliveryWeight(booking.getDeliveryWeight());
        return dto;
    }

    public static OrderBooking toOrderBooking(UUID orderId, BookedProductsDto productsDto) {
        OrderBooking booking = new OrderBooking();
        booking.setOrderId(orderId);
        booking.setFragile(productsDto.getFragile());
        booking.setDeliveryWeight(productsDto.getDeliveryWeight());
        booking.setDeliveryVolume(productsDto.getDeliveryVolume());
        return booking;
    }
}
