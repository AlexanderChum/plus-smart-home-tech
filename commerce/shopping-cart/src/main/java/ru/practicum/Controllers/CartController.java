package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto get(String username) {
        log.info("Попытка получить тележку пользователя");
        return service.get(username);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto add(String username, Map<UUID, Long> products) {
        log.info("Попытка создать тележку");
        return service.add(username, products);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(String username) {
        log.info("Попытка деактивировать тележку");
        service.deleteCart(username);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        log.info("Попытка убрать товары из тележку");
        return service.removeProducts(username, productIds);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto quantityUpdate(String username, ChangeProductQuantityRequest request) {
        log.info("Попытка изменить количество товаров в тележке");
        return service.changeQuantity(username, request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public String getUser(UUID id) {
        log.info("Получение пользователя");
        return service.getUser(id);
    }
}
