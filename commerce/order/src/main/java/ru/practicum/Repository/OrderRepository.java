package ru.practicum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.Model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUsername(String username);
}
