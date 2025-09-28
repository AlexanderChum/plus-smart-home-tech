package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Model.Delivery;
import ru.practicum.delivery.Models.DeliveryDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeliveryMapper {

    DeliveryDto deliveryToDto(Delivery delivery);

    Delivery deliveryFromDto(DeliveryDto dto);
}
