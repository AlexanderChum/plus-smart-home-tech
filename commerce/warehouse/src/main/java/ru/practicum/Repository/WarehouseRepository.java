package ru.practicum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.Models.StorageProduct;

import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<StorageProduct, UUID> {

}
