package it.carlogennaro.awesomemusicbookingservice.booking;

import jakarta.validation.constraints.NotNull;

public record UpdateBookingStatusRequest(

        @NotNull
        BookingStatus status
) {
}