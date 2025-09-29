package ru.practicum.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.cart.Models.ChangeProductQuantityRequest;
import ru.practicum.cart.Models.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartFeign {

    @GetMapping
    ShoppingCartDto get(@RequestParam String username);

    @PutMapping
    ShoppingCartDto add(@RequestParam String username, @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void deleteCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProducts(@RequestParam String username, @RequestBody List<UUID> productIds);

    @PostMapping("/change-quantity")
    ShoppingCartDto quantityUpdate(@RequestParam String username,
                                   @RequestBody @Valid ChangeProductQuantityRequest request);

    @GetMapping("/username")
    String getUser(@NotNull UUID id);
}
