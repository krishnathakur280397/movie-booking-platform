package com.krishna.moviebooking.dto;

import java.time.LocalDateTime;

public class ShowResponse {

	private Long id;
	private String movieTitle;
	private String screen;
	private LocalDateTime showTime;
	private int totalSeats;
	private int availableSeats;

	public ShowResponse(Long id, String movieTitle, String screen, LocalDateTime showTime, int totalSeats,
			int availableSeats) {
		this.id = id;
		this.movieTitle = movieTitle;
		this.screen = screen;
		this.showTime = showTime;
		this.totalSeats = totalSeats;
		this.availableSeats = availableSeats;
	}

	public Long getId() {
		return id;
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

	public int getTotalSeats() {
		return totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}
}
