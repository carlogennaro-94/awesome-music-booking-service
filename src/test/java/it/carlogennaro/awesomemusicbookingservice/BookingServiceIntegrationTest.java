package it.carlogennaro.awesomemusicbookingservice;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.carlogennaro.awesomemusicbookingservice.AwesomeMusicBookingServiceApplication;
import it.carlogennaro.awesomemusicbookingservice.booking.Booking;
import it.carlogennaro.awesomemusicbookingservice.booking.BookingRepository;
import it.carlogennaro.awesomemusicbookingservice.booking.BookingService;
import it.carlogennaro.awesomemusicbookingservice.booking.BookingSlot;
import it.carlogennaro.awesomemusicbookingservice.booking.BookingStatus;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AwesomeMusicBookingServiceApplication.class)
class BookingServiceIntegrationTest {

    private static final LocalDate BOOKING_DATE = LocalDate.of(2026, 9, 10);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void cleanDatabase() {
        bookingRepository.deleteAll();
    }

    @Test
    void createBooking_createsPendingBooking() {
        Booking booking = bookingService.createBooking(
                "ROOM-1",
                "Carlo",
                BOOKING_DATE,
                BookingSlot.EVENING
        );

        assertNotNull(booking.getBookingCode());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals("ROOM-1", booking.getRoomId());
        assertEquals("Carlo", booking.getCustomerName());
        assertEquals(BOOKING_DATE, booking.getBookingDate());
        assertEquals(BookingSlot.EVENING, booking.getSlot());
    }

    @Test
    void approve_pendingBooking_changesStatusToApproved() {
        Booking booking = bookingService.createBooking(
                "ROOM-1", "Mario", BOOKING_DATE, BookingSlot.EVENING
        );

        Booking approvedBooking = bookingService.approve(booking.getBookingCode());

        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void reject_pendingBooking_changesStatusToRejected() {
        Booking booking = bookingService.createBooking(
                "ROOM-1", "Carlo", BOOKING_DATE, BookingSlot.EVENING
        );

        Booking rejectedBooking = bookingService.reject(booking.getBookingCode());

        assertEquals(BookingStatus.REJECTED, rejectedBooking.getStatus());
    }

    @Test
    void approve_whenSlotAlreadyApproved_throwsException() {
        Booking firstBooking = bookingService.createBooking(
                "ROOM-1", "Mario", BOOKING_DATE, BookingSlot.EVENING
        );

        Booking secondBooking = bookingService.createBooking(
                "ROOM-1", "Carlo", BOOKING_DATE, BookingSlot.EVENING
        );

        bookingService.approve(firstBooking.getBookingCode());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> bookingService.approve(secondBooking.getBookingCode())
        );

        assertEquals(
                "The room is already booked for the selected date and slot",
                exception.getMessage()
        );
    }

    @Test
    void processAlreadyApprovedBooking_throwsException() {
        Booking booking = bookingService.createBooking(
                "ROOM-1", "Mario", BOOKING_DATE, BookingSlot.EVENING
        );

        bookingService.approve(booking.getBookingCode());

        assertThrows(
                IllegalStateException.class,
                () -> bookingService.approve(booking.getBookingCode())
        );

        assertThrows(
                IllegalStateException.class,
                () -> bookingService.reject(booking.getBookingCode())
        );
    }

    @Test
    void getPendingBookings_returnsOnlyPendingBookings() {
        Booking pendingBooking = bookingService.createBooking(
                "ROOM-1", "Carlo", BOOKING_DATE, BookingSlot.MORNING
        );

        Booking approvedBooking = bookingService.createBooking(
                "ROOM-1", "Mario", BOOKING_DATE, BookingSlot.EVENING
        );

        bookingService.approve(approvedBooking.getBookingCode());

        var pendingBookings = bookingService.getPendingBookings();

        assertEquals(1, pendingBookings.size());
        assertEquals(pendingBooking.getBookingCode(), pendingBookings.getFirst().getBookingCode());
        assertEquals(BookingStatus.PENDING, pendingBookings.getFirst().getStatus());
    }

    @Test
    void getByBookingCode_whenBookingDoesNotExist_throwsException() {
        assertThrows(
                NoSuchElementException.class,
                () -> bookingService.getByBookingCode("missing-code")
        );
    }
}