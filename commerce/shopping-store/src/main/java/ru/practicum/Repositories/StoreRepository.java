package ru.practicum.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.Models.Product;
import ru.practicum.store.Models.ProductCategory;

import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);
}
