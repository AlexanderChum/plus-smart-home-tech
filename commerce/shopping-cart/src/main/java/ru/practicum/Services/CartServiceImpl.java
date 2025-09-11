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
        usernameCheck(username);
        Optional<Cart> cart = repository.findByUsername(username);
        if (cart.isPresent()) {
            return mapper.toDto(cart.get());
        } else {
            return mapper.toDto(new Cart(null, username, true, new HashMap<>()));
        }
    }

    @Override
    public ShoppingCartDto add(String username, Map<UUID, Long> products) {

    }

    @Override
    public Boolean deleteCart(String username) {

    }

    @Override
    public ShoppingCartDto removeProducts(String username, List<UUID> productIds) {

    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {

    }

    private boolean usernameCheck(String username) {
        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("Пустое имя пользователя");
        }
        return true;
    }
}
