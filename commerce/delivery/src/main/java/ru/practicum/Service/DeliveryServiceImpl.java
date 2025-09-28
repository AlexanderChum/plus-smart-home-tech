package ru.practicum.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Mapper.DeliveryMapper;
import ru.practicum.Model.Delivery;
import ru.practicum.Repository.DeliveryRepository;
import ru.practicum.delivery.Models.DeliveryDto;
import ru.practicum.delivery.Models.DeliveryState;
import ru.practicum.delivery.Models.Errors.DeliveryNotFoundException;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.order.OrderFeign;
import ru.practicum.warehouse.Models.ShippedToDeliveryRequest;
import ru.practicum.warehouse.WarehouseFeign;

import java.util.UUID;

import static ru.practicum.Model.DeliveryPricesAndModifiers.ANOTHER_STREET_MODIFIER;
import static ru.practicum.Model.DeliveryPricesAndModifiers.BASIC_DELIVERY;
import static ru.practicum.Model.DeliveryPricesAndModifiers.FRAGILE_MODIFIER;
import static ru.practicum.Model.DeliveryPricesAndModifiers.VOLUME_MODIFIER;
import static ru.practicum.Model.DeliveryPricesAndModifiers.WAREHOUSE1_MODIFIER;
import static ru.practicum.Model.DeliveryPricesAndModifiers.WAREHOUSE2_MODIFIER;
import static ru.practicum.Model.DeliveryPricesAndModifiers.WEIGHT_MODIFIER;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    DeliveryRepository repository;
    DeliveryMapper mapper;
    WarehouseFeign warehouseClient;
    OrderFeign orderClient;

    public DeliveryDto createDelivery(DeliveryDto dto) {
        log.info("Получен запрос на создание доставки");
        Delivery delivery = mapper.deliveryFromDto(dto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        Delivery addedDelivery = repository.save(delivery);
        log.info("Доставка успешно сохранена в бд");
        return mapper.deliveryToDto(addedDelivery);
    }

    public void changeSuccess(UUID id) {
        log.info("Смена статуса на success, service");
        Delivery delivery = checkDelivery(id);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        orderClient.orderCompleted(id);
    }

    public void changePicked(UUID id) {
        log.info("Смена статуса на picked, service");
        Delivery delivery = checkDelivery(id);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        orderClient.orderAssembly(id);
        warehouseClient.shipRequesting(new ShippedToDeliveryRequest(id, delivery.getDeliveryId()));
    }

    public void changeFailed(UUID id) {
        log.info("Смена статуса на failed, service");
        Delivery delivery = checkDelivery(id);
        delivery.setDeliveryState(DeliveryState.FAILED);
        orderClient.orderDeliveryFailed(id);
    }

    public Double getCost(OrderDto dto) {
        Delivery delivery = checkDelivery(dto.getOrderId());
        log.info("Пройдена проверка на существование доставки");

        Double totalCost = BASIC_DELIVERY;

        if (delivery.getFromAddress().getStreet().equals("ADDRESS_1")) {
            totalCost += totalCost * WAREHOUSE1_MODIFIER;
        } else if (delivery.getFromAddress().getStreet().equals("ADDRESS_2")) {
            totalCost += totalCost * WAREHOUSE2_MODIFIER;
        }

        if (dto.getFragile()) {
            totalCost += totalCost * FRAGILE_MODIFIER;
        }

        totalCost += totalCost * WEIGHT_MODIFIER;
        totalCost += totalCost * VOLUME_MODIFIER;

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            totalCost += totalCost * ANOTHER_STREET_MODIFIER;
        }
        log.info("Проведен подсчет итоговой доставки");

        return totalCost;
    }

    private Delivery checkDelivery(UUID id) {
        return repository.findByOrderId(id)
                .orElseThrow(DeliveryNotFoundException::new);
    }
}
