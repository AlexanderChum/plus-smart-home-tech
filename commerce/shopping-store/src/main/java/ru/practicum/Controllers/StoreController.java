package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.store.Models.ProductCategory;
import ru.practicum.store.Models.ProductDto;
import ru.practicum.store.Models.SetProductQuantityStateRequest;
import ru.practicum.Services.StoreService;
import ru.practicum.store.StoreFeign;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoreController implements StoreFeign {
    StoreService service;

    @Override
    public Page<ProductDto> getByCategory(ProductCategory category, Pageable pageable) {
        return service.getByCategory(category, pageable);
    }

    @Override
    public ProductDto add(ProductDto dto) {
        return service.add(dto);
    }

    @Override
    public ProductDto update(ProductDto dto) {
        return service.update(dto);
    }

    @Override
    public Boolean remove(UUID productId) {
        return service.remove(productId);
    }

    @Override
    public Boolean quantityUpdate(SetProductQuantityStateRequest request) {
        return service.quantityUpdate(request);
    }

    @Override
    public ProductDto getById(UUID productId) {
        return service.getById(productId);
    }
}
