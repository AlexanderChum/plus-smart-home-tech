package ru.practicum.warehouse.Models.Errors;

public class ProductShoppingCartLowQuantityInWarehouse extends RuntimeException {
    public ProductShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }
}
