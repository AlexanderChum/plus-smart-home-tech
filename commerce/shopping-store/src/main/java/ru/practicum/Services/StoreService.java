package ru.practicum.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.store.Models.ProductCategory;
import ru.practicum.store.Models.ProductDto;
import ru.practicum.store.Models.SetProductQuantityStateRequest;

import java.util.UUID;

public interface StoreService {

    Page<ProductDto> getByCategory(ProductCategory category, Pageable pageable);

    ProductDto add(ProductDto dto);

    ProductDto update(ProductDto dto);

    Boolean remove(UUID productId);

    Boolean quantityUpdate(SetProductQuantityStateRequest request);

    ProductDto getById(UUID productId);
}
