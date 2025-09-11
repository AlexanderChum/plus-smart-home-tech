package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Models.StorageProduct;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface WarehouseMapper {
    @Mapping(target = "width", source = "productDto.dimension.width")
    @Mapping(target = "height", source = "productDto.dimension.height")
    @Mapping(target = "depth", source = "productDto.dimension.depth")
    @Mapping(target = "quantity", ignore = true)
    StorageProduct fromDto(NewProductInWarehouseRequest productDto);
}
