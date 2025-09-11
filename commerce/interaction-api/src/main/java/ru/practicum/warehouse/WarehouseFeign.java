package ru.practicum.warehouse;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeign {

    @PutMapping
    void put(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto check(@RequestBody @Valid ShoppingCartDto cart);

    @PostMapping("/add")
    void add(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}
