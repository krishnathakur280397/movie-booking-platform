package com.krishna.moviebooking.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shows")
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@Column(nullable = false)
	private String screen;

	@Column(nullable = false)
	private LocalDateTime showTime;

	@Column(nullable = false)
	private int totalSeats;

	@Column(nullable = false)
	private int availableSeats;

	protected Show() {
	}

	public Show(Movie movie, String screen, LocalDateTime showTime, int totalSeats) {
		this.movie = movie;
		this.screen = screen;
		this.showTime = showTime;
		this.totalSeats = totalSeats;
		this.availableSeats = totalSeats;
	}

	public Long getId() {
		return id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public LocalDateTime getShowTime() {
		return showTime;
	}

	public void setShowTime(LocalDateTime showTime) {
		this.showTime = showTime;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getAvailableSeats() {
		return availableSeats;
	}

	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}

	public void reserveSeats(int seats) {
		if (seats <= 0) {
			throw new IllegalArgumentException("seats must be > 0");
		}
		if (availableSeats < seats) {
			throw new IllegalStateException("not enough seats available");
		}
		this.availableSeats -= seats;
	}
}
