package it.carlogennaro.awesomemusicbookingservice.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String bookingCode;

    private String roomId;

    private String customerName;

    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING)
    private BookingSlot slot;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    protected Booking() {
        // Required by JPA
    }

    public Booking(
            String roomId,
            String customerName,
            LocalDate bookingDate,
            BookingSlot slot
    ) {
        this.bookingCode = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.customerName = customerName;
        this.bookingDate = bookingDate;
        this.slot = slot;
        this.status = BookingStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public BookingSlot getSlot() {
        return slot;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void approve() {
        this.status = BookingStatus.APPROVED;
    }

    public void reject() {
        this.status = BookingStatus.REJECTED;
    }
}