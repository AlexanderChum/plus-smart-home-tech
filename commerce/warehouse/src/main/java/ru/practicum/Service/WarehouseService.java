package ru.practicum.Service;

import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;

public interface WarehouseService {

    void put(NewProductInWarehouseRequest request);

    BookedProductsDto check(ShoppingCartDto cartDto);

    void add(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}
