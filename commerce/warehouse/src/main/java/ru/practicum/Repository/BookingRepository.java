package ru.practicum.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.Models.OrderBooking;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<OrderBooking, UUID> {
}
