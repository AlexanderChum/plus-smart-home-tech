package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Service.PaymentService;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.payment.Models.PaymentDto;
import ru.practicum.payment.PaymentFeign;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController implements PaymentFeign {
    PaymentService service;

    @Override
    public PaymentDto createPayment(OrderDto dto) {
        log.info("Запрос на создание платежки");
        return service.createPayment(dto);
    }

    @Override
    public Double getTotalCost(OrderDto dto) {
        log.info("Запрос на получение суммарной стоимости");
        return service.getTotalCost(dto);
    }

    @Override
    public void getSuccess(UUID id) {
        log.info("Запрос на смену статуса на success");
        service.getSuccess(id);
    }

    @Override
    public Double getProductsCost(OrderDto dto) {
        log.info("Запрос на получение стоимости продуктов");
        return service.getProductsCost(dto);
    }

    @Override
    public void getFailed(UUID id) {
        log.info("Запрос на смену статуса на failed");
        service.getFailed(id);
    }
}
