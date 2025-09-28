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
import ru.practicum.warehouse.Models.AssemblyProductsForOrderRequest;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;
import ru.practicum.warehouse.Models.ShippedToDeliveryRequest;
import ru.practicum.warehouse.WarehouseFeign;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class WarehouseController implements WarehouseFeign {
    WarehouseService service;

    @Override
    public Boolean put(NewProductInWarehouseRequest request) {
        log.info("Добавление нового продукта на склад");
        service.put(request);
        return true;
    }

    @Override
    public void shipRequesting(ShippedToDeliveryRequest request) {
        log.info("Запрос на передачу в доставку");
        service.shipmentRequesting(request);
    }

    @Override
    public void returnRequesting(Map<UUID, Long> products) {
        log.info("Возвращение букированных продуктов");
        service.returnRequesting(products);
    }

    @Override
    public BookedProductsDto check(ShoppingCartDto cartDto) {
        log.info("Проверка продуктов для тележки");
        return service.check(cartDto);
    }

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        log.info("Сборка заказа");
        return service.assembly(request);
    }

    @Override
    public Boolean add(AddProductToWarehouseRequest request) {
        log.info("Обновление количества товаров на складе");
        service.add(request);
        return true;
    }

    @Override
    public AddressDto getAddress() {
        log.info("Получение адреса");
        return service.getAddress();
    }
}