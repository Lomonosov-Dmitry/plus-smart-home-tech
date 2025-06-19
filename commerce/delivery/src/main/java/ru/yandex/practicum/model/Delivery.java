package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    UUID deliveryId;

    @Column(name = "order_id")
    UUID orderId;

    @Column(name = "from_country")
    String fromCountry;

    @Column(name = "from_city")
    String fromCity;

    @Column(name = "from_street")
    String fromStreet;

    @Column(name = "from_house")
    String fromHouse;

    @Column(name = "from_flat")
    String fromFlat;

    @Column(name = "to_country")
    String toCountry;

    @Column(name = "to_city")
    String toCity;

    @Column(name = "to_street")
    String toStreet;

    @Column(name = "to_house")
    String toHouse;

    @Column(name = "to_flat")
    String toFlat;

    @Column(name = "delivery_state")
    DeliveryState deliveryState;

    @Column(name = "delivery_weight")
    Double deliveryWeight;

    @Column(name = "delivery_volume")
    Double deliveryVolume;

    @Column(name = "fragile")
    Boolean fragile;
}
