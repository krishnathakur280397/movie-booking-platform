package com.krishna.moviebooking.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krishna.moviebooking.dto.BookingResponse;
import com.krishna.moviebooking.dto.CreateBookingRequest;
import com.krishna.moviebooking.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@Validated
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
		return bookingService.bookTickets(request.getShowId(), request.getCustomerName(), request.getSeats());
	}

	@GetMapping
	public List<BookingResponse> listBookings(@RequestParam(required = false) Long showId) {
		return bookingService.listBookings(showId);
	}

	@GetMapping("/{id}")
	public BookingResponse getBooking(@PathVariable Long id) {
		return bookingService.getBooking(id);
	}
}
