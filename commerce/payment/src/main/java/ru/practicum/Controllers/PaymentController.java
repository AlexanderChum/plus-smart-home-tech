package ru.practicum.Controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(OrderDto dto) {
        log.info("Запрос на создание платежки");
        return service.createPayment(dto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Double getTotalCost(OrderDto dto) {
        log.info("Запрос на получение суммарной стоимости");
        return service.getTotalCost(dto);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getSuccess(UUID id) {
        log.info("Запрос на смену статуса на success");
        service.getSuccess(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Double getProductsCost(OrderDto dto) {
        log.info("Запрос на получение стоимости продуктов");
        return service.getProductsCost(dto);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getFailed(UUID id) {
        log.info("Запрос на смену статуса на failed");
        service.getFailed(id);
    }
}
