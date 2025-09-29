package ru.practicum.Service;

import jakarta.ws.rs.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Mapper.WarehouseMapper;
import ru.practicum.Models.OrderBooking;
import ru.practicum.Models.StorageProduct;
import ru.practicum.Repository.BookingRepository;
import ru.practicum.Repository.WarehouseRepository;
import ru.practicum.cart.Models.ShoppingCartDto;
import ru.practicum.warehouse.Models.AddProductToWarehouseRequest;
import ru.practicum.warehouse.Models.AddressDto;
import ru.practicum.warehouse.Models.AssemblyProductsForOrderRequest;
import ru.practicum.warehouse.Models.BookedProductsDto;
import ru.practicum.warehouse.Models.Errors.NoSpecifiedProductInWarehouseException;
import ru.practicum.warehouse.Models.Errors.ProductShoppingCartLowQuantityInWarehouse;
import ru.practicum.warehouse.Models.Errors.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.warehouse.Models.NewProductInWarehouseRequest;
import ru.practicum.warehouse.Models.ShippedToDeliveryRequest;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
    WarehouseRepository warehouseRepository;
    BookingRepository bookingRepository;
    WarehouseMapper mapper;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public AddressDto getAddress() {
        log.debug("Возвращение адреса");
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    @Override
    public void put(NewProductInWarehouseRequest request) {
        log.info("Попытка добавить новый товар на склад");
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException();
        } else {
            warehouseRepository.save(mapper.fromDto(request));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto check(ShoppingCartDto shoppingCartDto) {
        log.info("Попытка замерить товары из корзины");
        return checkProducts(shoppingCartDto.getProducts());
    }

    @Override
    public void add(AddProductToWarehouseRequest request) {
        StorageProduct product = warehouseRepository.findById(request.getProductId())
                .orElseThrow(NoSpecifiedProductInWarehouseException::new);
        log.info("Проверка пройдена, изменяем количество товара на складе");
        product.setQuantity(product.getQuantity() + request.getQuantity());
    }

    @Override
    public void shipmentRequesting(ShippedToDeliveryRequest request) {
        OrderBooking booking = bookingRepository.findById(request.getOrderId()).
                orElseThrow(() -> new NotFoundException("Заказ не был найден на складе"));
        log.info("Добавление id доставки заказу");
        booking.setDeliveryId(request.getDeliveryId());
    }

    @Override
    public void returnRequesting(Map<UUID, Long> products) {
        log.info("Системный возврат товаров на склад");
        products.forEach((productId, quantity) -> {
            AddProductToWarehouseRequest request = new AddProductToWarehouseRequest(productId, quantity);
            add(request);
        });
    }

    @Override
    public BookedProductsDto assembly(AssemblyProductsForOrderRequest request) {
        BookedProductsDto booked = checkProducts(request.getProducts());
        log.info("Проверка товаров из запроса пройдена");

        request.getProducts().forEach((productId, quantity) -> {
            StorageProduct product = warehouseRepository.findById(productId)
                    .orElseThrow(NoSpecifiedProductInWarehouseException::new);
            product.setQuantity(product.getQuantity() - quantity);
        });
        log.info("Подсчет нового количества продуктов произведен");
        bookingRepository.save(new OrderBooking(null, request.getOrderId(), null, request.getProducts()));
        return booked;
    }

    private BookedProductsDto checkProducts(Map<UUID, Long> products) {
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;

        log.info("Отправка товаров на проверку и замеры");
        for (UUID productUUID : products.keySet()) {
            StorageProduct product = warehouseRepository.findById(productUUID)
                    .orElseThrow(NoSpecifiedProductInWarehouseException::new);
            if (product.getQuantity() < products.get(productUUID)) {
                throw new ProductShoppingCartLowQuantityInWarehouse();
            }
            deliveryWeight += product.getWeight();
            deliveryVolume += product.getWidth() * product.getHeight() * product.getDepth();
            fragile |= product.getFragile();
        }
        log.info("Возвращение итогов замеров");

        return new BookedProductsDto(deliveryWeight, deliveryVolume, fragile);
    }
}
