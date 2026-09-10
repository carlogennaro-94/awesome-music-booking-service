package it.carlogennaro.awesomemusicbookingservice.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> findByBookingCode(String bookingCode);

    boolean existsByRoomIdAndBookingDateAndSlotAndStatus(
            String roomId,
            LocalDate bookingDate,
            BookingSlot slot,
            BookingStatus status
    );
}