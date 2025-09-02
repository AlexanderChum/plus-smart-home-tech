package ru.practicum.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.Models.Product;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Product, UUID> {
}
