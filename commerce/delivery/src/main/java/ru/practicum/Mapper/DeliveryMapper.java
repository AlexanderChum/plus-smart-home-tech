package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Model.Address;
import ru.practicum.Model.Delivery;
import ru.practicum.delivery.Models.DeliveryDto;
import ru.practicum.warehouse.Models.AddressDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeliveryMapper {

    DeliveryDto deliveryToDto(Delivery delivery);

    Delivery deliveryFromDto(DeliveryDto dto);

    @Mapping(target = "addressId", ignore = true)
    Address addressFromDto(AddressDto dto);

    AddressDto addressToDto(Address address);
}
