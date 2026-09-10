package it.carlogennaro.awesomemusicbookingservice.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookingRequest(

        @NotBlank
        String roomId,

        @NotBlank
        String customerName,

        @NotNull
        @FutureOrPresent
        LocalDate bookingDate,

        @NotNull
        BookingSlot slot
) {
}