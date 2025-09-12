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
        log.debug("Попытка получить тележку пользователя");
        return service.get(username);
    }

    @Override
    public ShoppingCartDto add(String username, Map<UUID, Long> products) {
        log.debug("Попытка создать тележку");
        return service.add(username, products);
    }

    @Override
    public void deleteCart(String username) {
        log.debug("Попытка деактивировать тележку");
        service.deleteCart(username);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        log.debug("Попытка убрать товары из тележку");
        return service.removeProducts(username, productIds);
    }

    @Override
    public ShoppingCartDto quantityUpdate(String username, ChangeProductQuantityRequest request) {
        log.debug("Попытка изменить количество товаров в тележке");
        return service.changeQuantity(username, request);
    }
}
