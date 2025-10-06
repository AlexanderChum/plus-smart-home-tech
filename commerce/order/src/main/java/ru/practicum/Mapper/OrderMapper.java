package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Model.Order;
import ru.practicum.order.Models.OrderDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderMapper {
    OrderDto toDto(Order order);
}
