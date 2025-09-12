package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Service.WarehouseService;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;
import ru.practicum.warehouse.WarehouseFeign;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WarehouseController implements WarehouseFeign {
    WarehouseService service;

    @Override
    public void put(NewProductInWarehouseRequest request) {
        log.debug("Добавление нового продукта на склад");
        service.put(request);
    }

    @Override
    public BookedProductsDto check(ShoppingCartDto cartDto) {
        log.debug("Проверка продуктов для тележки");
        return service.check(cartDto);
    }

    @Override
    public void add(AddProductToWarehouseRequest request) {
        log.debug("Обновление количества товаров на складе");
        service.add(request);
    }

    @Override
    public AddressDto getAddress() {
        log.debug("Получение адреса");
        return service.getAddress();
    }
}