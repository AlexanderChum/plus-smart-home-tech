package ru.practicum.Services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.Mapper.StoreMapper;
import ru.practicum.store.Models.Errors.ProductNotFoundException;
import ru.practicum.Models.Product;
import ru.practicum.store.Models.ProductCategory;
import ru.practicum.store.Models.ProductDto;
import ru.practicum.store.Models.ProductState;
import ru.practicum.store.Models.SetProductQuantityStateRequest;
import ru.practicum.Repositories.StoreRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StoreServiceImpl implements StoreService {
    StoreRepository repository;
    StoreMapper mapper;

    @Override
    public Page<ProductDto> getByCategory(ProductCategory category, Pageable pageable) {
        Page<Product> products = repository.findAllByProductCategory(category, pageable);
        return products.map(mapper::toDto);
    }

    @Override
    public ProductDto add(ProductDto dto) {
        Product product = mapper.fromDto(dto);
        return mapper.toDto(findIfExists(repository.save(product).getId()));
    }

    @Override
    public ProductDto update(ProductDto dto) {
        findIfExists(dto.getProductId());
        Product savedProduct = repository.save(mapper.fromDto(dto));
        return mapper.toDto(findIfExists(savedProduct.getId()));
    }

    @Override
    public Boolean remove(UUID productId) {
        Product product = findIfExists(productId);
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        return true;
    }

    @Override
    public Boolean quantityUpdate(SetProductQuantityStateRequest request) {
        Product product = findIfExists(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        repository.save(product);
        return true;
    }

    @Override
    public ProductDto getById(UUID productId) {
        return mapper.toDto(findIfExists(productId));
    }

    private Product findIfExists(UUID productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Продукт с таким id = " + productId + " не найден"));
    }
}
