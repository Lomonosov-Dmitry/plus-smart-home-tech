package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductCategory(ProductCategory category);
}
