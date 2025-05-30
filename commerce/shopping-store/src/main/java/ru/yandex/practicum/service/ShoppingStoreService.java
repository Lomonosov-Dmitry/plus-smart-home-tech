package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.ProductState;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mappers.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductsRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ShoppingStoreService {

    private final ProductsRepository productsRepository;

    public ProductDto newProduct(ProductDto dto) {
        return ProductMapper.INSTANCE.toDto(productsRepository.save(ProductMapper.INSTANCE.toProduct(dto)));

    }

    public ProductDto updateProduct(ProductDto dto) {
        if (!productsRepository.existsById(dto.getProductId()))
            throw new ProductNotFoundException("Продукт не найден");
        productsRepository.deleteById(dto.getProductId());
        return ProductMapper.INSTANCE.toDto(productsRepository.save(ProductMapper.INSTANCE.toProduct(dto)));
    }

    public Boolean removeProduct(UUID id) {
        if (!productsRepository.existsById(id))
            throw new ProductNotFoundException("Продукт не найден");
        else {
            Product prod = productsRepository.findById(id).get();
            prod.setProductState(ProductState.DEACTIVATE);
            productsRepository.save(prod);
            return true;
        }
    }

    public ProductDto updateProductQuantity(UUID productId, String state) {
        if (!productsRepository.existsById(productId))
            throw new ProductNotFoundException("Продукт не найден");
        else {
            QuantityState newState = Arrays.stream(QuantityState.values())
                    .filter(quantityState -> quantityState.name().equalsIgnoreCase(state))
                    .findFirst()
                    .orElse(null);
            if (newState == null)
                throw new NotFoundException("Неверно указано количество товара.");
            Product prod = productsRepository.findById(productId).get();
            prod.setQuantityState(newState);
            return ProductMapper.INSTANCE.toDto(productsRepository.save(prod));
        }
    }


    public ProductDto getProductById(UUID id) {
        Optional<Product> prod = productsRepository.findById(id);
        if (prod.isEmpty())
            throw new ProductNotFoundException("Продукт не найден");
        else
            return ProductMapper.INSTANCE.toDto(prod.get());
    }

    public List<ProductDto> getAllProducts(String prodCategory, int page, int size, List<String> sort) {
        ProductCategory category = Arrays.stream(ProductCategory.values())
                .filter(prodCat -> prodCat.name().equalsIgnoreCase(prodCategory))
                .findFirst()
                .orElse(null);
        if (category == null)
            throw new NotFoundException("Категория товара " + prodCategory + " не найдена");
        Pageable pageable = null;
        if (!sort.isEmpty())
            pageable = PageRequest.of(page, size, Sort.by(Sort.DEFAULT_DIRECTION, String.join(",", sort)));
        else
            pageable = PageRequest.of(page, size);
        List<Product> products = productsRepository.findAllByProductCategory(ProductCategory.valueOf(prodCategory), pageable);

        return products.stream()
                .map(ProductMapper.INSTANCE::toDto)
                .toList();
    }
}
