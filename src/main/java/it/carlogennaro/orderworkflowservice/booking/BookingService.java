package it.carlogennaro.orderworkflowservice.booking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(
            String roomId,
            String customerName,
            LocalDate bookingDate,
            BookingSlot slot
    ) {
        Booking booking = new Booking(roomId, customerName, bookingDate, slot);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public Booking getByBookingCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));
    }

    @Transactional(readOnly = true)
    public List<Booking> getPendingBookings() {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus() == BookingStatus.PENDING)
                .toList();
    }

    public Booking approve(String bookingCode) {
        Booking booking = getByBookingCode(bookingCode);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking has already been processed");
        }

        boolean alreadyApproved = bookingRepository
                .existsByRoomIdAndBookingDateAndSlotAndStatus(
                        booking.getRoomId(),
                        booking.getBookingDate(),
                        booking.getSlot(),
                        BookingStatus.APPROVED
                );

        if (alreadyApproved) {
            throw new IllegalStateException(
                    "The room is already booked for the selected date and slot"
            );
        }

        booking.approve();
        return booking;
    }

    public Booking reject(String bookingCode) {
        Booking booking = getByBookingCode(bookingCode);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Booking has already been processed");
        }

        booking.reject();
        return booking;
    }
}