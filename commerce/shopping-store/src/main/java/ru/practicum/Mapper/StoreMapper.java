package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Models.Product;
import ru.practicum.store.Models.ProductDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface StoreMapper {

    @Mapping(source = "id", target = "productId")
    ProductDto toDto(Product product);

    @Mapping(source = "productId", target = "id")
    Product fromDto(ProductDto productDto);

    @Mapping(source = "productId", target = "id")
    void updateFromDto(@MappingTarget Product product, ProductDto dto);
}