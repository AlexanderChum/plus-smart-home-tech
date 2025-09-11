package ru.practicum.cart.Models.Error;

public class NotAuthorizedUserException extends RuntimeException {
  public NotAuthorizedUserException(String message) {
    super(message);
  }
}
