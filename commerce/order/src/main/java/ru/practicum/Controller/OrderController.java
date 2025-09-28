package ru.practicum.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Service.OrderService;
import ru.practicum.order.Models.CreateNewOrderRequest;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.order.Models.ProductReturnRequest;
import ru.practicum.order.OrderFeign;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class OrderController implements OrderFeign {
    OrderService service;

    @Override
    public List<OrderDto> getOrders(String username) {
        log.info("Запрос на получение заказов клиента");
        return service.getOrders(username);
    }

    @Override
    public OrderDto addOrder(CreateNewOrderRequest request) {
        log.info("Запрос на создание заказа");
        return service.addOrder(request);
    }

    @Override
    public OrderDto returnProduct(ProductReturnRequest request) {
        log.info("Запрос на возврат продуктов из заказа");
        return service.returnProduct(request);
    }

    @Override
    public OrderDto orderPayment(UUID id) {
        log.info("Запрос на оплату заказа");
        return service.orderPayment(id);
    }

    @Override
    public OrderDto orderPaymentFailed(UUID id) {
        log.info("Запрос на провал оплаты заказа");
        return service.orderPaymentFailed(id);
    }

    @Override
    public OrderDto orderDelivery(UUID id) {
        log.info("Запрос на доставку заказа");
        return service.orderDelivery(id);
    }

    @Override
    public OrderDto orderDeliveryFailed(UUID id) {
        log.info("Запрос на провал доставки заказа");
        return service.orderDeliveryFailed(id);
    }

    @Override
    public OrderDto orderCompleted(UUID id) {
        log.info("Запрос на завершение заказа");
        return service.orderCompleted(id);
    }

    @Override
    public OrderDto orderCalculatedTotal(UUID id) {
        log.info("Запрос на подсчет итоговой цены заказа");
        return service.orderCalculatedTotal(id);
    }

    @Override
    public OrderDto orderCalculatedDelivery(UUID id) {
        log.info("Запрос на подсчет цены доставки заказа");
        return service.orderCalculatedDelivery(id);
    }

    @Override
    public OrderDto orderAssembly(UUID id) {
        log.info("Запрос на сборку заказа");
        return service.orderAssembly(id);
    }

    @Override
    public OrderDto orderAssemblyFailed(UUID id) {
        log.info("Запрос на провал сборки заказа");
        return service.orderAssemblyFailed(id);
    }
}
