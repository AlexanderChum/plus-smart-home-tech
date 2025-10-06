package ru.practicum.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.AssemblyProductsForOrderRequest;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;
import ru.practicum.warehouse.Models.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeign {

    @PutMapping
    Boolean put(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/shipped")
    void shipRequesting(@Valid @RequestBody ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnRequesting(@NotNull @RequestBody Map<UUID, Long> products);

    @PostMapping("/check")
    BookedProductsDto check(@RequestBody @Valid ShoppingCartDto cart);

    @PostMapping("/assembly")
    BookedProductsDto assembly(@Valid @RequestBody AssemblyProductsForOrderRequest request);

    @PostMapping("/add")
    Boolean add(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();


}
