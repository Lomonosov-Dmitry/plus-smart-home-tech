package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    ShoppingCartDto toDto(Cart cart);

    Cart toCart(ShoppingCartDto dto);

}
