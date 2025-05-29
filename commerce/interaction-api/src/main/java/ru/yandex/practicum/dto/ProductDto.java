package ru.yandex.practicum.dto;

import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.enums.QuantityState;

import java.util.UUID;

@Getter
@Setter
public class ProductDto {

    UUID productId;

    @NotNull
    String productName;

    @NotNull
    String description;

    String imageSrc;

    @NotNull
    QuantityState quantityState;

    @NotNull
    ProductState productState;

    ProductCategory productCategory;

    @NotNull
    Double price;
}
