package com.krishna.moviebooking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krishna.moviebooking.dto.BookingResponse;
import com.krishna.moviebooking.entity.Booking;
import com.krishna.moviebooking.entity.Show;
import com.krishna.moviebooking.repository.BookingRepository;
import com.krishna.moviebooking.repository.ShowRepository;

@Service
public class BookingService {

	private final ShowRepository showRepository;
	private final BookingRepository bookingRepository;

	public BookingService(ShowRepository showRepository, BookingRepository bookingRepository) {
		this.showRepository = showRepository;
		this.bookingRepository = bookingRepository;
	}

	@Transactional
	public BookingResponse bookTickets(Long showId, String customerName, int seats) {
		Show show = showRepository.findByIdForUpdate(showId)
				.orElseThrow(() -> new IllegalArgumentException("show not found: " + showId));

		if (seats <= 0) {
			throw new IllegalArgumentException("seats must be > 0");
		}
		if (seats > show.getAvailableSeats()) {
			throw new IllegalStateException("not enough seats available");
		}

		show.reserveSeats(seats);
		Booking booking = new Booking(show, customerName, seats);
		bookingRepository.save(booking);

		return toResponse(booking);
	}

	@Transactional(readOnly = true)
	public List<BookingResponse> listBookings(Long showId) {
		List<Booking> bookings = (showId == null) ? bookingRepository.findAllWithShowAndMovie()
				: bookingRepository.findByShowIdWithShowAndMovie(showId);
		return bookings.stream().map(BookingService::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public BookingResponse getBooking(Long bookingId) {
		Booking booking = bookingRepository.findByIdWithShowAndMovie(bookingId)
				.orElseThrow(() -> new IllegalArgumentException("booking not found: " + bookingId));
		return toResponse(booking);
	}

	private static BookingResponse toResponse(Booking booking) {
		var show = booking.getShow();
		return new BookingResponse(booking.getId(), show.getId(), show.getMovie().getTitle(), show.getScreen(),
				show.getShowTime(), booking.getSeatsBooked(), booking.getCreatedAt());
	}
}
