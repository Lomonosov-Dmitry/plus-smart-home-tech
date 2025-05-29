package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.mappers.WarehouseProductMapper;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.WarehouseRepository;

import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class WarehouseService {
    @Autowired
    private WarehouseRepository repository;

    public void addProduct(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId()))
            throw new SpecifiedProductAlreadyInWarehouseException("Такой товар уже есть на складе");
        WarehouseProduct product = WarehouseProductMapper.toWarehouseProduct(request);
        repository.save(product);
    }

    public void incProductQuantity(AddProductToWarehouseRequest request) {
        if (!repository.existsById(request.getProductId()))
            throw new NotFoundException("Продукт с id = " + request.getProductId() + " не найден");
        WarehouseProduct product = repository.findById(request.getProductId()).get();
        product.setQuantity(request.getQuantity());
        repository.deleteById(request.getProductId());
        repository.save(product);
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
        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setDeliveryVolume(0.0);
        bookedProductsDto.setDeliveryWeight(0.0);
        bookedProductsDto.setFragile(false);
        for (UUID dto : cartDto.getProducts().keySet()) {
            WarehouseProduct product = repository.findById(dto).orElseThrow(() ->
                    new NoSpecifiedProductInWarehouseException("Товара с id = " + dto + " нет на складе"));
            if (cartDto.getProducts().get(dto) > product.getQuantity())
                throw new ProductInShoppingCartLowQuantityInWarehouse("Недостаточное количество товара " + dto + " на складе");
            bookedProductsDto.setDeliveryVolume(bookedProductsDto.getDeliveryVolume() +
                    (product.getDimention().getHeight() * product.getDimention().getWidth() * product.getDimention().getDepth()));
            bookedProductsDto.setDeliveryWeight(bookedProductsDto.getDeliveryWeight() + product.getWeight());
            if (!bookedProductsDto.getFragile() && product.getFragile())
                bookedProductsDto.setFragile(true);
        }
        return bookedProductsDto;
    }
}
