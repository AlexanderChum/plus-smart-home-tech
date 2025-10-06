package ru.practicum.delivery;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.delivery.Models.DeliveryDto;
import ru.practicum.order.Models.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeign {

    @PutMapping
    DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto dto);

    @PostMapping("/successful")
    void changeSuccess(UUID id);

    @PostMapping("/picked")
    void changePicked(UUID id);

    @PostMapping("/failed")
    void changeFailed(UUID id);

    @PostMapping("/cost")
    Double getCost(@Valid @RequestBody OrderDto dto);
}
