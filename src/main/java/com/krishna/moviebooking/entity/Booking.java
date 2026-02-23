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
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "show_id", nullable = false)
	private Show show;

	@Column(nullable = false)
	private String customerName;

	@Column(nullable = false)
	private int seatsBooked;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	protected Booking() {
	}

	public Booking(Show show, String customerName, int seatsBooked) {
		this.show = show;
		this.customerName = customerName;
		this.seatsBooked = seatsBooked;
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Show getShow() {
		return show;
	}

	public String getCustomerName() {
		return customerName;
	}

	public int getSeatsBooked() {
		return seatsBooked;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
