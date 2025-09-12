package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Models.Cart;
import ru.practicum.cart.Models.ShoppingCartDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CartMapper {
    ShoppingCartDto toDto(Cart cart);
}
