package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

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

    public Boolean removeProduct(Long id) {
        if (!productsRepository.existsById(id))
            throw new ProductNotFoundException("Продукт не найден");
        else {
            Product prod = productsRepository.findById(id).get();
            prod.setProductState(ProductState.DEACTIVATE);
            productsRepository.save(prod);
            return true;
        }
    }

    public ProductDto updateProductQuantity(Long productId, String state) {
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


    public ProductDto getProductById(Long id) {
        Optional<Product> prod = productsRepository.findById(id);
        if (prod.isEmpty())
            throw new ProductNotFoundException("Продукт не найден");
        else
            return ProductMapper.INSTANCE.toDto(prod.get());
    }

    public Page<ProductDto> getAllProducts(String prodCategory, int page, int size, String sort) {
        ProductCategory category = Arrays.stream(ProductCategory.values())
                .filter(prodCat -> prodCat.name().equalsIgnoreCase(prodCategory))
                .findFirst()
                .orElse(null);
        if (category == null)
            throw new NotFoundException("Категория товара " + prodCategory + " не найдена");

        List<ProductDto> products = null;
        if (sort != null)
            products = productsRepository.findAll(Sort.by(sort)).stream()
                    .filter(product -> product.getProductCategory().equals(category))
                    .map(ProductMapper.INSTANCE::toDto)
                    .toList();
        else
            products = productsRepository.findAll().stream()
                    .filter(product -> product.getProductCategory().equals(category))
                    .map(ProductMapper.INSTANCE::toDto)
                    .toList();

        return new PageImpl<>(products, (PageRequest.of(page, size)), products.size());
    }
}
