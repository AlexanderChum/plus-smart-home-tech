package ru.practicum.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Mapper.WarehouseMapper;
import ru.practicum.Models.StorageProduct;
import ru.practicum.Repository.WarehouseRepository;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.Errors.NoSpecifiedProductInWarehouseException;
import ru.practicum.warehouse.Models.Errors.ProductShoppingCartLowQuantityInWarehouse;
import ru.practicum.warehouse.Models.Errors.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    WarehouseRepository repository;
    WarehouseMapper mapper;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public AddressDto getAddress() {
        log.debug("Возвращение адреса");
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    @Override
    public void put(NewProductInWarehouseRequest request) {
        if (repository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Продукт уже существует на складе");
        } else {
            repository.save(mapper.fromDto(request));
        }
    }

    @Override
    public BookedProductsDto check(ShoppingCartDto shoppingCartDto) {
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;

        Map<UUID, Long> products = shoppingCartDto.getProducts();
        log.debug("Отправка товаров на проверку и замеры");
        for (UUID productUUID : products.keySet()) {
            StorageProduct product = repository.findById(productUUID)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Продукт не существует на складе"));
            if (product.getQuantity() < products.get(productUUID)) {
                throw new ProductShoppingCartLowQuantityInWarehouse("Продукта недостаточно на складе");
            }
            deliveryWeight += product.getWeight();
            deliveryVolume += product.getWidth() * product.getHeight() * product.getDepth();
            fragile |= product.getFragile();
        }
        log.debug("Возвращение итогов замеров");

        return new BookedProductsDto(deliveryWeight, deliveryVolume, fragile);
    }

    @Override
    public void add(AddProductToWarehouseRequest request) {
        StorageProduct product = repository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Продукт не существует на складе"));
        log.debug("Проверка пройдена, изменяем количество товара на складе");
        product.setQuantity(product.getQuantity() + request.getQuantity());
        repository.save(product);
    }
}
