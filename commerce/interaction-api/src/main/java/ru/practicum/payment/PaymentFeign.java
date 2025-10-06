package ru.practicum.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.payment.Models.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeign {

    @PostMapping
    PaymentDto createPayment(@Valid @RequestBody OrderDto dto);

    @PostMapping("/totalCost")
    Double getTotalCost(@Valid @RequestBody OrderDto dto);

    @PostMapping("/refund")
    void getSuccess(@RequestBody UUID id);

    @PostMapping("/productCost")
    Double getProductsCost(@Valid @RequestBody OrderDto dto);

    @PostMapping("/failed")
    void getFailed(@RequestBody UUID id);
}
