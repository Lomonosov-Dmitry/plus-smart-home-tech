package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mappers.CartMapper;
import ru.yandex.practicum.model.Cart;
import ru.yandex.practicum.repository.CartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final CartRepository repository;

    @Autowired
    private final WarehouseClient client;

    public ShoppingCartDto addProduct(String username, Map<UUID, Integer> prodsMap) {
        if (!prodsMap.isEmpty()) {
            ShoppingCartDto cartDto = new ShoppingCartDto();
            cartDto.setProducts(prodsMap);
            client.checkQuantity(cartDto);
            Cart cart = CartMapper.INSTANCE.toCart(cartDto);
            cart.setStatus(true);
            cart.setUsername(username);
            return CartMapper.INSTANCE.toDto(repository.save(cart));
        } else
            throw new NotFoundException("Список товаров пуст");
    }

    public void deactivateCart(String username) {
        Cart cart = repository.findCartByUsername(username).orElseThrow(() ->
                new NotFoundException("Корзина для пользователя " + username + " не найдена"));
        cart.setStatus(false);
        repository.save(cart);
    }

    public ShoppingCartDto removeProducts(String username, List<String> ids) {
        Cart cart = repository.findCartByUsername(username).orElseThrow(() ->
                new NotFoundException("Корзина для пользователя " + username + " не найдена"));
        if (!ids.isEmpty()) {
            Map<UUID, Integer> prods = cart.getProducts();
            for (UUID uuid : prods.keySet()) {
                if (ids.contains(uuid.toString()))
                    prods.remove(uuid);
            }
            cart.setProducts(prods);
        } else {
            throw new NotFoundException("Список товаров пуст");
        }
        return CartMapper.INSTANCE.toDto(repository.save(cart));
    }

    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        Cart cart = repository.findCartByUsername(username).orElseThrow(() ->
                new NotFoundException("Корзина для пользователя " + username + " не найдена"));
        if (!cart.getProducts().containsKey(request.getProductId()))
            throw new NotFoundException("Товар с ID = " + request.getProductId() + " в корзине не найден");
        else
            cart.getProducts().replace(request.getProductId(), request.getNewQuantity());
        return CartMapper.INSTANCE.toDto(repository.save(cart));
    }

    public ShoppingCartDto getUserCart(String username) {
        Cart cart = repository.findCartByUsername(username).orElseThrow(() ->
                new NotFoundException("Корзина для пользователя " + username + " не найдена"));
        return CartMapper.INSTANCE.toDto(cart);
    }

}
