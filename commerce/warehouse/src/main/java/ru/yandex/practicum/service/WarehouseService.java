package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mappers.OrderBookingMapper;
import ru.yandex.practicum.mappers.WarehouseProductMapper;
import ru.yandex.practicum.model.OrderBooking;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseOrdersRepository;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.requests.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.requests.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class WarehouseService {
    @Autowired
    private WarehouseRepository productRepository;
    @Autowired
    WarehouseOrdersRepository ordersRepository;

    public void addProduct(NewProductInWarehouseRequest request) {
        if (productRepository.existsById(request.getProductId()))
            throw new SpecifiedProductAlreadyInWarehouseException("Такой товар уже есть на складе");
        WarehouseProduct product = WarehouseProductMapper.toWarehouseProduct(request);
        productRepository.save(product);
    }

    public void incProductQuantity(AddProductToWarehouseRequest request) {
        WarehouseProduct product = productRepository.findById(request.getProductId()).orElseThrow(() ->
                new NotFoundException("Продукт с id = " + request.getProductId() + " не найден"));
        product.setQuantity(request.getQuantity());
        productRepository.deleteById(request.getProductId());
        productRepository.save(product);
    }

    public AddressDto getAddress() {
        AddressDto dto = new AddressDto();
        dto.setCountry("ADDRESS1");
        dto.setCity("ADDRESS1");
        dto.setStreet("ADDRESS1");
        dto.setHouse("ADDRESS1");
        dto.setFlat("ADDRESS1");
        return dto;
    }

    public BookedProductsDto checkCart(ShoppingCartDto cartDto) {
        return getDtoForProducts(cartDto.getProducts());
    }

    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        BookedProductsDto bookedProductsDto = getDtoForProducts(request.getProducts());
        for (UUID productId : request.getProducts().keySet()) {
            WarehouseProduct product = productRepository.getReferenceById(productId);
            product.setQuantity(product.getQuantity() - request.getProducts().get(productId));
            productRepository.save(product);
        }
        OrderBooking booking = OrderBookingMapper.toOrderBooking(request.getOrderId(), bookedProductsDto);
        ordersRepository.save(booking);
        return bookedProductsDto;
    }

    public void setShipped(ShippedToDeliveryRequest request) {
        OrderBooking booking = ordersRepository.findById(request.getOrderId()).orElseThrow(() ->
                new NotFoundException("Не найден заказ с ID = " + request.getOrderId()));
        if (booking.getDeliveryId() != null)
            throw new IllegalArgumentException("Заказ с ID = " + request.getOrderId() + " уже отгружен!");
        booking.setDeliveryId(request.getDeliveryId());
        ordersRepository.save(booking);
    }

    public void returnProducts(Map<UUID, Integer> products) {
        for (UUID productId : products.keySet()) {
            WarehouseProduct product = productRepository.findById(productId).orElseThrow(() ->
                    new NoSpecifiedProductInWarehouseException("Товара с id = " + productId + " нет на складе"));
            product.setQuantity(product.getQuantity() + products.get(productId));
            productRepository.save(product);
        }
    }

    private BookedProductsDto getDtoForProducts(Map<UUID, Integer> products) {
        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setDeliveryVolume(0.0);
        bookedProductsDto.setDeliveryWeight(0.0);
        bookedProductsDto.setFragile(false);
        for (UUID dto : products.keySet()) {
            WarehouseProduct product = productRepository.findById(dto).orElseThrow(() ->
                    new NoSpecifiedProductInWarehouseException("Товара с id = " + dto + " нет на складе"));
            if (products.get(dto) > product.getQuantity())
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточное количество товара " + dto + " на складе");
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() + product.getVolume());
            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() + product.getWeight());
            if (!bookedProductsDto.getFragile() && product.getFragile())
                bookedProductsDto.setFragile(true);
        }
        return bookedProductsDto;
    }
}
