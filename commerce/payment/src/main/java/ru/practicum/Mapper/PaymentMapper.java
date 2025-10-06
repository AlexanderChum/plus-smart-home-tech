package ru.practicum.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.Models.Payment;
import ru.practicum.payment.Models.PaymentDto;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
}
