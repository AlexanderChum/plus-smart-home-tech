package ru.practicum.store;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.store.Models.ProductCategory;
import ru.practicum.store.Models.ProductDto;
import ru.practicum.store.Models.SetProductQuantityStateRequest;

import java.util.UUID;

@FeignClient(name = "shopping-store", path = "api/v1/shopping-store")
public interface StoreFeign {

    @GetMapping
    Page<ProductDto> getByCategory(@RequestParam ProductCategory category, Pageable pageable);

    @PutMapping
    ProductDto add(@RequestBody @Valid ProductDto productDto);

    @PostMapping
    ProductDto update(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    Boolean remove(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    Boolean quantityUpdate(@Valid SetProductQuantityStateRequest request);

    @GetMapping("/{productId}")
    ProductDto getById(@PathVariable UUID productId);
}
