package ru.practicum.Services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.Mapper.CartMapper;
import ru.practicum.Models.Cart;
import ru.practicum.Repositories.CartRepository;
import ru.practicum.cart.Models.ChangeProductQuantityRequest;
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
public class CartServiceImpl implements CartService {
    CartRepository repository;
    CartMapper mapper;

    @Override
    public ShoppingCartDto get(String username) {
        return mapper.toDto(usernameCheckAndGetCart(username));
    }

    @Override
    public ShoppingCartDto add(String username, Map<UUID, Long> products) {
        Cart cart = usernameCheckAndGetCart(username);
        if (cart.getState().equals(false)) {
            throw new RuntimeException("Тележка недоступна для обновления");
        } else {
            for (UUID productId : products.keySet()) {
                if (cart.getProducts().containsKey(productId)) {
                    Long quantity = cart.getProducts().get(productId) + products.get(productId);
                    cart.getProducts().put(productId, quantity);
                } else {
                    cart.getProducts().put(productId, products.get(productId));
                }
            }
        }
        return mapper.toDto(repository.save(cart));
    }

    @Override
    public Boolean deleteCart(String username) {
        Cart cart = usernameCheckAndGetCart(username);
        cart.setState(false);
        repository.save(cart);
        return true;
    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {
        Cart cart = usernameCheckAndGetCart(username);
        if (cart.getState().equals(false)) {
            throw new RuntimeException("Тележка недоступна для обновления");
        } else {
            for (UUID productId : productIds) {
                cart.getProducts().remove(productId);
            }
        }
        return mapper.toDto(repository.save(cart));
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        Cart cart = usernameCheckAndGetCart(username);
        if (cart.getState().equals(false)) {
            throw new RuntimeException("Тележка недоступна для обновлений");
        } else {
            cart.getProducts().put(request.getProductId(), request.getQuantity());
            return mapper.toDto(repository.save(cart));
        }
    }

    private Cart usernameCheckAndGetCart(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Пустое имя пользователя");
        }
        Optional<Cart> cart = repository.findByUsername(username);
        return cart.orElseGet(() -> new Cart(null, username, true, new HashMap<>()));
    }
}
