package ru.practicum.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.practicum.delivery.Models.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {

    @Id
    @UuidGenerator
    UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id")
    Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id")
    Address toAddress;

    @Enumerated(EnumType.STRING)
    DeliveryState deliveryState;
}
