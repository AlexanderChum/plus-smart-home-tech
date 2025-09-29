package ru.practicum.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.order.Models.CreateNewOrderRequest;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.order.Models.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeign {

    @GetMapping
    List<OrderDto> getOrders(@RequestParam String username);

    @PutMapping
    OrderDto addOrder(@Valid @RequestBody CreateNewOrderRequest request);

    @PostMapping("/return")
    OrderDto returnProduct(@Valid @RequestParam ProductReturnRequest request);

    @PostMapping("/payment")
    OrderDto orderPayment(@NotNull @RequestBody UUID id);

    @PostMapping("/payment/failed")
    OrderDto orderPaymentFailed(@NotNull @RequestBody UUID id);

    @PostMapping("/delivery")
    OrderDto orderDelivery(@NotNull @RequestBody UUID id);

    @PostMapping("/delivery/failed")
    OrderDto orderDeliveryFailed(@NotNull @RequestBody UUID id);

    @PostMapping("/completed")
    OrderDto orderCompleted(@NotNull @RequestBody UUID id);

    @PostMapping("/calculate/total")
    OrderDto orderCalculatedTotal(@NotNull @RequestBody UUID id);

    @PostMapping("/calculate/delivery")
    OrderDto orderCalculatedDelivery(@NotNull @RequestBody UUID id);

    @PostMapping("/assembly")
    OrderDto orderAssembly(@NotNull @RequestBody UUID id);

    @PostMapping("/assembly/failed")
    OrderDto orderAssemblyFailed(@NotNull @RequestBody UUID id);
}
