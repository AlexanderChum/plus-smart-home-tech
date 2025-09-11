package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Services.CartService;
import ru.practicum.cart.CartFeign;
import ru.practicum.cart.Models.ChangeProductQuantityRequest;
import ru.practicum.cart.Models.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController implements CartFeign {
    CartService service;

    @Override
    public ShoppingCartDto get(String username) {
        return service.get(username);
    }

    @Override
    public ShoppingCartDto add(String username, Map<UUID, Long> products) {
        return service.add(username, products);
    }

    @Override
    public Boolean deleteCart(String username) {
        return service.deleteCart(username);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        return service.removeProducts(username, productIds);
    }

    @Override
    public ShoppingCartDto quantityUpdate(String username, ChangeProductQuantityRequest request) {
        return service.changeQuantity(username, request);
    }
}
