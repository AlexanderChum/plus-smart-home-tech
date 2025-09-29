package ru.practicum.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Mapper.OrderMapper;
import ru.practicum.Model.Order;
import ru.practicum.Repository.OrderRepository;
import ru.practicum.cart.CartFeign;
import ru.practicum.cart.Models.Error.NotAuthorizedUserException;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.delivery.DeliveryFeign;
import ru.practicum.delivery.Models.DeliveryDto;
import ru.practicum.delivery.Models.DeliveryState;
import ru.practicum.order.Models.CreateNewOrderRequest;
import ru.practicum.order.Models.OrderDto;
import ru.practicum.order.Models.OrderState;
import ru.practicum.order.Models.ProductReturnRequest;
import ru.practicum.order.Models.errors.OrderNotFoundException;
import ru.practicum.payment.PaymentFeign;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.WarehouseFeign;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class OrderServiceImpl implements OrderService {
    OrderMapper mapper;
    OrderRepository repository;
    WarehouseFeign warehouseClient;
    PaymentFeign paymentClient;
    DeliveryFeign deliveryClient;
    CartFeign cartClient;

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getOrders(String username) {
        log.info("Получение заказов пользователя");
        if (username.isBlank()) throw new NotAuthorizedUserException();
        return repository.findByUsername(username).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public OrderDto addOrder(CreateNewOrderRequest request) {
        ShoppingCartDto cartDto = request.getCartDto();
        String user = cartClient.getUser(cartDto.getCartId());
        BookedProductsDto booked = warehouseClient.check(cartDto);
        log.info("Обработка запроса на создание заказа");

        Order order = repository.save(new Order(
                null,
                user,
                cartDto.getCartId(),
                cartDto.getProducts(),
                null,
                null,
                OrderState.NEW,
                booked.getDeliveryWeight(),
                booked.getDeliveryVolume(),
                booked.getFragile(),
                null,
                null,
                null
        ));
        log.info("Заказ создан");

        DeliveryDto deliveryDto = deliveryClient.createDelivery(new DeliveryDto(
                null,
                warehouseClient.getAddress(),
                request.getAddressDto(),
                order.getOrderId(),
                DeliveryState.CREATED
        ));
        log.info("Добавление id доставки к заказу");

        order.setDeliveryId(deliveryDto.getDeliveryId());
        return mapper.toDto(order);
    }

    @Override
    public OrderDto returnProduct(ProductReturnRequest request) {
        log.info("Разбукирование продуктов");
        Order order = checkOrder(request.getOrderId());
        order.setState(OrderState.PRODUCT_RETURNED);
        warehouseClient.returnRequesting(request.getProducts());
        return mapper.toDto(order);
    }

    @Override
    public OrderDto orderPayment(UUID id) {
        log.info("Оплата заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.PAID));
    }

    @Override
    public OrderDto orderPaymentFailed(UUID id) {
        log.info("Провал оплаты заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.PAYMENT_FAILED));
    }

    @Override
    public OrderDto orderDelivery(UUID id) {
        log.info("Доставка заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.DELIVERED));
    }

    @Override
    public OrderDto orderDeliveryFailed(UUID id) {
        log.info("Провал доставки заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.DELIVERY_FAILED));
    }

    @Override
    public OrderDto orderCompleted(UUID id) {
        log.info("Завершение заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.COMPLETED));
    }

    @Override
    public OrderDto orderCalculatedTotal(UUID id) {
        log.info("Заполнение полей стоимости заказа");
        Order order = checkOrder(id);
        order.setProductPrice(paymentClient.getProductsCost(mapper.toDto(order)));
        order.setTotalPrice(paymentClient.getTotalCost(mapper.toDto(order)));
        return mapper.toDto(order);
    }

    @Override
    public OrderDto orderCalculatedDelivery(UUID id) {
        log.info("Подсчет стоимости доставки заказа");
        Order order = checkOrder(id);
        order.setDeliveryPrice(deliveryClient.getCost(mapper.toDto(order)));
        return mapper.toDto(order);
    }

    @Override
    public OrderDto orderAssembly(UUID id) {
        log.info("Сборка заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.ASSEMBLED));
    }

    @Override
    public OrderDto orderAssemblyFailed(UUID id) {
        log.info("Провал сборки заказа");
        return mapper.toDto(checkOrderSetState(id, OrderState.ASSEMBLY_FAILED));
    }

    private Order checkOrder(UUID id) {
        return repository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }

    private Order checkOrderSetState(UUID id, OrderState state) {
        Order order = checkOrder(id);
        order.setState(state);
        return order;
    }
}
