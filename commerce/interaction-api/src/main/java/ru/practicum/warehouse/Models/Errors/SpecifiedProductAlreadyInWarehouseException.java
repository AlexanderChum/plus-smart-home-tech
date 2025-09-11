package ru.practicum.warehouse.Models.Errors;

public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }
}
