package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.yandex.practicum.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.model.Dimention;
import ru.yandex.practicum.model.WarehouseProduct;

@Mapper//(componentModel = "spring")
public abstract class WarehouseProductMapper {

    public static WarehouseProduct toWarehouseProduct(NewProductInWarehouseRequest request) {
        WarehouseProduct product = new WarehouseProduct();
        product.setProductId(request.getProductId());
        product.setFragile(request.getFragile());
        Dimention dimention = new Dimention();
        dimention.setWidth(request.getDimension().getWidth());
        dimention.setHeight(request.getDimension().getHeight());
        dimention.setDepth(request.getDimension().getDepth());
        product.setDimention(dimention);
        product.setWeight(request.getWeight());
        return product;
    }

    /*WarehouseProductMapper INSTATCE = Mappers.getMapper(WarehouseProductMapper.class);

    WarehouseProduct toWarehouseProduct(NewProductInWarehouseRequest request);*/
}
