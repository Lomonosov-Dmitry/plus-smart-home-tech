package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "warehouse_products")
public class WarehouseProduct {
    @Id
    @Column(name = "product_id")
    UUID productId;

    @Column(name = "fragile")
    Boolean fragile;

    @Embedded
    Dimention dimention;

    @Column(name = "weight")
    Double weight;

    @Column(name = "quantity")
    Integer quantity;
}
