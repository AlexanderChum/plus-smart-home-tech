package ru.practicum.Services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Mapper.CartMapper;
import ru.practicum.Models.Cart;
import ru.practicum.Repositories.CartRepository;
import ru.practicum.cart.Models.ChangeProductQuantityRequest;
import ru.practicum.cart.Models.Error.NoProductsInShoppingCartException;
import ru.practicum.cart.Models.Error.NotAuthorizedUserException;
import ru.practicum.cart.Models.ShoppingCartDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class CartServiceImpl implements CartService {
    CartRepository repository;
    CartMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public ShoppingCartDto get(String username) {
        Cart cart = usernameCheckAndGetCart(username);
        log.info("Получена тележка для пользователя");
        return mapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto add(String username, Map<UUID, Long> products) {
        Cart cart = usernameCheckAndGetCart(username);
        log.info("Получена тележка для пользователя");
        repository.save(cart);
        for (UUID productId : products.keySet()) {
            if (cart.getProducts().containsKey(productId)) {
                Long quantity = cart.getProducts().get(productId) + products.get(productId);
                cart.getProducts().put(productId, quantity);
            } else {
                cart.getProducts().put(productId, products.get(productId));
            }
        }
        return mapper.toDto(cart);
    }

    @Override
    public void deleteCart(String username) {
        Cart cart = usernameCheckAndGetCart(username);
        log.info("Получена тележка для пользователя");
        cart.setState(false);
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        Cart cart = usernameCheckAndGetCart(username);
        log.info("Получена тележка для пользователя");
        for (UUID productId : productIds) {
            cart.getProducts().remove(productId);
        }
        return mapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        Cart cart = usernameCheckAndGetCart(username);
        log.info("Получена тележка для пользователя");
        if (!cart.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException();
        }
        cart.getProducts().put(request.getProductId(), request.getNewQuantity());
        log.info("Продукты в тележки изменены");
        return mapper.toDto(cart);
    }

    private Cart usernameCheckAndGetCart(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException();
        }
        Optional<Cart> cart = repository.findByUsername(username);
        return cart.orElseGet(() -> new Cart(null, username, true, new HashMap<>()));
    }
}
