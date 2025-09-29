package ru.practicum.Service;

import ru.practicum.order.Models.OrderDto;
import ru.practicum.payment.Models.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto dto);

    Double getTotalCost(OrderDto dto);

    Double getProductsCost(OrderDto dto);

    void getSuccess(UUID id);

    void getFailed(UUID id);
}
