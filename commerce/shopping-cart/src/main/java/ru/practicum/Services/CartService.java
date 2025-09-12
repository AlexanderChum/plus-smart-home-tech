package ru.practicum.Services;

import ru.practicum.cart.Models.ChangeProductQuantityRequest;
import ru.practicum.cart.Models.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {

    ShoppingCartDto get(String username);

    ShoppingCartDto add(String username, Map<UUID, Long> products);

    void deleteCart(String username);

    ShoppingCartDto removeProducts(String username, List<UUID> productIds);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request);
}
