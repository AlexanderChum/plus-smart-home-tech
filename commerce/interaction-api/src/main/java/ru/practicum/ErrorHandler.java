package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.cart.Models.Error.NoProductsInShoppingCartException;
import ru.practicum.cart.Models.Error.NotAuthorizedUserException;
import ru.practicum.store.Models.Errors.ProductNotFoundException;
import ru.practicum.warehouse.Models.Errors.NoSpecifiedProductInWarehouseException;
import ru.practicum.warehouse.Models.Errors.ProductShoppingCartLowQuantityInWarehouse;
import ru.practicum.warehouse.Models.Errors.SpecifiedProductAlreadyInWarehouseException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(ProductNotFoundException e) {
        return new ErrorResponse("Продукт не найден");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoProductsInShoppingCart(NoProductsInShoppingCartException e) {
        return new ErrorResponse("В продуктовой корзине нет товаров");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUser(NotAuthorizedUserException e) {
        return new ErrorResponse("Неавторизованный пользователь");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductInWarehouse(NoSpecifiedProductInWarehouseException e) {
        return new ErrorResponse("Продукт не существует на складе");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLowQuantityProductsForCart(ProductShoppingCartLowQuantityInWarehouse e) {
        return new ErrorResponse("Продукта недостаточно на складе");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductWarehouse(SpecifiedProductAlreadyInWarehouseException e) {
        return new ErrorResponse("Продукт уже существует на складе");
    }
}
