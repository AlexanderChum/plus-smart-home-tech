package ru.practicum.Service;

import ru.practicum.delivery.Models.DeliveryDto;
import ru.practicum.order.Models.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto dto);

    void changeSuccess(UUID id);

    void changePicked(UUID id);

    void changeFailed(UUID id);

    Double getCost(OrderDto dto);
}
