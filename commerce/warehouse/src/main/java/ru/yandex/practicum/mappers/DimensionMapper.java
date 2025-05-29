package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.DimentionDto;

import java.awt.*;

@Mapper(componentModel = "spring")
public interface DimensionMapper {
    Dimension toDimension(DimentionDto dto);
}
