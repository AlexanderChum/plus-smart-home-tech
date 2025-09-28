package ru.practicum.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Service.DeliveryService;
import ru.practicum.delivery.DeliveryFeign;
import ru.practicum.delivery.Models.DeliveryDto;
import ru.practicum.order.Models.OrderDto;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryController implements DeliveryFeign {
    DeliveryService service;

    @Override
    public DeliveryDto createDelivery(DeliveryDto dto) {
        log.info("Создание доставки");
        return service.createDelivery(dto);
    }

    @Override
    public void changeSuccess(UUID id) {
        log.info("Смена статуса на success");
        service.changeSuccess(id);
    }

    @Override
    public void changePicked(UUID id) {
        log.info("Смена статуса на picked");
        service.changePicked(id);
    }

    @Override
    public void changeFailed(UUID id) {
        log.info("Смена статуса на failed");
        service.changeFailed(id);
    }

    @Override
    public Double getCost(OrderDto dto) {
        log.info("Получение стоимости доставки");
        return service.getCost(dto);
    }
}
