package ru.practicum.Service;

import ru.practicum.order.Models.CreateNewOrderRequest;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.order.Models.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getOrders(String username);

    OrderDto addOrder(CreateNewOrderRequest request);

    OrderDto returnProduct(ProductReturnRequest request);

    OrderDto orderPayment(UUID id);

    OrderDto orderPaymentFailed(UUID id);

    OrderDto orderDelivery(UUID id);

    OrderDto orderDeliveryFailed(UUID id);

    OrderDto orderCompleted(UUID id);

    OrderDto orderCalculatedTotal(UUID id);

    OrderDto orderCalculatedDelivery(UUID id);

    OrderDto orderAssembly(UUID id);

    OrderDto orderAssemblyFailed(UUID id);
}
