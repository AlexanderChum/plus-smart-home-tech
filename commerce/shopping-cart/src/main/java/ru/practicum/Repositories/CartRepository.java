package ru.practicum.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.Models.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUsername(String username);
}
