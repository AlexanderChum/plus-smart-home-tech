package ru.practicum.order.Models;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddressDto;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateNewOrderRequest {
    @NotNull
    ShoppingCartDto cartDto;

    @NotNull
    AddressDto addressDto;
}
