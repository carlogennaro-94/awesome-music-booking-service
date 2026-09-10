package it.carlogennaro.orderworkflowservice.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        Booking booking = bookingService.createBooking(
                request.roomId(),
                request.customerName(),
                request.bookingDate(),
                request.slot()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BookingResponse.from(booking));
    }

    @GetMapping("/pending")
    public List<BookingResponse> getPendingBookings() {
        return bookingService.getPendingBookings().stream()
                .map(BookingResponse::from)
                .toList();
    }

    @GetMapping("/{bookingCode}")
    public BookingResponse getBookingStatus(
            @PathVariable String bookingCode
    ) {
        return BookingResponse.from(
                bookingService.getByBookingCode(bookingCode)
        );
    }

    @PatchMapping("/{bookingCode}/status")
    public BookingResponse updateBookingStatus(
            @PathVariable String bookingCode,
            @Valid @RequestBody UpdateBookingStatusRequest request
    ) {
        Booking booking = switch (request.status()) {
            case APPROVED -> bookingService.approve(bookingCode);
            case REJECTED -> bookingService.reject(bookingCode);
            case PENDING -> throw new IllegalArgumentException(
                    "A booking cannot be changed back to PENDING"
            );
        };

        return BookingResponse.from(booking);
    }
}