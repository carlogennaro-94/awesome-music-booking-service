package it.carlogennaro.orderworkflowservice.booking;

import java.time.LocalDate;

public record BookingResponse(
        String bookingCode,
        String roomId,
        String customerName,
        LocalDate bookingDate,
        BookingSlot slot,
        BookingStatus status
) {

    public static BookingResponse from(Booking booking) {
        return new BookingResponse(
                booking.getBookingCode(),
                booking.getRoomId(),
                booking.getCustomerName(),
                booking.getBookingDate(),
                booking.getSlot(),
                booking.getStatus()
        );
    }
}