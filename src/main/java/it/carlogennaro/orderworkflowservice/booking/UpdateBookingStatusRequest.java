package it.carlogennaro.orderworkflowservice.booking;

import jakarta.validation.constraints.NotNull;

public record UpdateBookingStatusRequest(

        @NotNull
        BookingStatus status
) {
}