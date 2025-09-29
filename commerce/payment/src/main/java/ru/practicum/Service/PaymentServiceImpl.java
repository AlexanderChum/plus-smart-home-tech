package ru.practicum.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Mapper.PaymentMapper;
import ru.practicum.Models.Payment;
import ru.practicum.Models.PaymentState;
import ru.practicum.Repository.PaymentRepository;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.order.OrderFeign;
import ru.practicum.payment.Models.Errors.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.payment.Models.Errors.PaymentNotFoundException;
import ru.practicum.payment.Models.PaymentDto;
import ru.practicum.store.StoreFeign;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    PaymentRepository repository;
    PaymentMapper mapper;
    OrderFeign orderClient;
    StoreFeign storeClient;
    private final Double NDS_MODIFIER = 0.1;

    @Override
    public PaymentDto createPayment(OrderDto dto) {
        log.info("Создание платежки");
        Payment payment = new Payment(
                null,
                dto.getOrderId(),
                dto.getTotalPrice(),
                dto.getDeliveryPrice(),
                dto.getProductPrice() * NDS_MODIFIER,
                PaymentState.PENDING
        );
        return mapper.toDto(repository.save(payment));
    }

    @Transactional(readOnly = true)
    @Override
    public Double getTotalCost(OrderDto dto) {
        log.info("Получение суммарной стоимости");
        Double totalCost = getProductsCost(dto);
        return totalCost + totalCost * NDS_MODIFIER + dto.getDeliveryPrice();
    }

    @Transactional(readOnly = true)
    @Override
    public Double getProductsCost(OrderDto dto) {
        log.info("Получение стоимости продуктов");
        if (dto.getProducts().isEmpty()) throw new NotEnoughInfoInOrderToCalculateException();
        return dto.getProducts().entrySet().stream()
                .mapToDouble(product -> {
                    Double price = storeClient.getById(product.getKey()).getPrice();
                    return price * product.getValue();
                })
                .sum();
    }

    @Override
    public void getSuccess(UUID id) {
        log.info("Смена статуса на success");
        Payment payment = checkPayment(id);
        payment.setState(PaymentState.SUCCESS);
        orderClient.orderPayment(id);
    }

    @Override
    public void getFailed(UUID id) {
        log.info("Смена статуса на failed");
        Payment payment = checkPayment(id);
        payment.setState(PaymentState.FAILED);
        orderClient.orderPaymentFailed(id);
    }

    private Payment checkPayment(UUID id) {
        return repository.findByOrderId(id)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
