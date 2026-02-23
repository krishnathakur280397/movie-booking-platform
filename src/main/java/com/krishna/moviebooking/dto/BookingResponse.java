package com.krishna.moviebooking.dto;

import java.time.LocalDateTime;

public class BookingResponse {

	private Long bookingId;
	private Long showId;
	private String movieTitle;
	private String screen;
	private LocalDateTime showTime;
	private int seatsBooked;
	private LocalDateTime createdAt;

	public BookingResponse(Long bookingId, Long showId, String movieTitle, String screen, LocalDateTime showTime,
			int seatsBooked, LocalDateTime createdAt) {
		this.bookingId = bookingId;
		this.showId = showId;
		this.movieTitle = movieTitle;
		this.screen = screen;
		this.showTime = showTime;
		this.seatsBooked = seatsBooked;
		this.createdAt = createdAt;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public Long getShowId() {
		return showId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public String getScreen() {
		return screen;
	}

	public LocalDateTime getShowTime() {
		return showTime;
	}

	public int getSeatsBooked() {
		return seatsBooked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
