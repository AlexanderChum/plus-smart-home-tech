package ru.practicum.payment.Models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    UUID paymentId;
    Double totalPayment;
    Double deliveryTotal;
    Double feeTotal;
}
