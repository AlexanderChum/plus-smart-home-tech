package ru.practicum.warehouse.Models;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippedToDeliveryRequest {
    @NotNull
    UUID orderId;

    @NotNull
    UUID deliveryId;
}
