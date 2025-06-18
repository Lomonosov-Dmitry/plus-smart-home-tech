package ru.yandex.practicum.circuit;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exceptions.Unavalible;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.requests.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

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
    public AddressDto getWarehouseAddress() {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(@RequestBody AssemblyProductsForOrderRequest request) {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void setShipped(@RequestBody ShippedToDeliveryRequest request)  {
        throw new Unavalible("Сервис временно недоступен");
    }

    @Override
    public void returnProducts(@RequestBody Map<UUID, Integer> products)  {
        throw new Unavalible("Сервис временно недоступен");
    }
}
