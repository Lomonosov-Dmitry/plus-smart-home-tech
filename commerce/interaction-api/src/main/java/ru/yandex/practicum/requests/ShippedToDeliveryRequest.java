package ru.yandex.practicum.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippedToDeliveryRequest {
    @NotNull
    UUID orderId;

    @NotNull
    UUID deliveryId;

    public static ShippedToDeliveryRequest of(UUID orderId, UUID deliveryId) {
        ShippedToDeliveryRequest request = new ShippedToDeliveryRequest();
        request.deliveryId = deliveryId;
        request.orderId = orderId;
        return request;
    }
}
