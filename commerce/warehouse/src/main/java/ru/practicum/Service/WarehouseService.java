package ru.practicum.Service;

import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.AssemblyProductsForOrderRequest;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;
import ru.practicum.warehouse.Models.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void put(NewProductInWarehouseRequest request);

    void shipmentRequesting(ShippedToDeliveryRequest request);

    void returnRequesting(Map<UUID, Long> products);

    BookedProductsDto check(ShoppingCartDto cartDto);

    BookedProductsDto assembly(AssemblyProductsForOrderRequest request);

    void add(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
