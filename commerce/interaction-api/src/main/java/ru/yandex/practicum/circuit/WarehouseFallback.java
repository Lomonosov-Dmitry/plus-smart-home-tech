package ru.yandex.practicum.circuit;

import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.WarehouseClient;

public class WarehouseFallback implements WarehouseClient {
    @Override
    public BookedProductsDto checkQuantity(ShoppingCartDto cart) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void incQuantity(AddProductToWarehouseRequest request) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public AddressDto getAddress() {
        throw new Unavalible("Сервис временно недоступен");
    }
}
