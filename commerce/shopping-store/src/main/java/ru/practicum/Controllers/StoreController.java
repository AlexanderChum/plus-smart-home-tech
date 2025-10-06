package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> getByCategory(ProductCategory category, Pageable pageable) {
        log.info("Получение продуктов на витрине");
        return service.getByCategory(category, pageable);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto add(ProductDto dto) {
        log.info("Добавление продуктов на витрину");
        return service.add(dto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ProductDto update(ProductDto dto) {
        log.info("Обновление продуктов на витрине");
        return service.update(dto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Boolean remove(UUID productId) {
        log.info("Удаление продуктов с витрины");
        return service.remove(productId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Boolean quantityUpdate(SetProductQuantityStateRequest request) {
        log.info("Обновление количества товаров на витрине");
        return service.quantityUpdate(request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getById(UUID productId) {
        log.info("Получение продукта по идентификатору");
        return service.getById(productId);
    }
}
