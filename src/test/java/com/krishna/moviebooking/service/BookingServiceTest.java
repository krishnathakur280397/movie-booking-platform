package com.krishna.moviebooking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.krishna.moviebooking.dto.BookingResponse;
import com.krishna.moviebooking.entity.Booking;
import com.krishna.moviebooking.entity.Movie;
import com.krishna.moviebooking.entity.Show;
import com.krishna.moviebooking.repository.BookingRepository;
import com.krishna.moviebooking.repository.ShowRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	@Mock
	ShowRepository showRepository;

	@Mock
	BookingRepository bookingRepository;

	@InjectMocks
	BookingService bookingService;

	@Test
	void givenAvailableSeats_whenBookTickets_thenCreatesBookingAndDecrementsAvailability() {
		Movie movie = new Movie("Test Movie");
		Show show = new Show(movie, "Screen-1", LocalDateTime.now().plusHours(1), 10);
		ReflectionTestUtils.setField(show, "id", 1L);

		when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));
		when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

		BookingResponse response = bookingService.bookTickets(1L, "Krishna", 3);

		assertThat(response.getShowId()).isEqualTo(1L);
		assertThat(response.getMovieTitle()).isEqualTo("Test Movie");
		assertThat(response.getSeatsBooked()).isEqualTo(3);
		assertThat(show.getAvailableSeats()).isEqualTo(7);

		ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
		verify(bookingRepository).save(bookingCaptor.capture());
		assertThat(bookingCaptor.getValue().getSeatsBooked()).isEqualTo(3);
		assertThat(bookingCaptor.getValue().getCustomerName()).isEqualTo("Krishna");
	}

	@Test
	void givenInsufficientSeats_whenBookTickets_thenThrowsConflict() {
		Movie movie = new Movie("Test Movie");
		Show show = new Show(movie, "Screen-1", LocalDateTime.now().plusHours(1), 2);

		when(showRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(show));

		assertThatThrownBy(() -> bookingService.bookTickets(1L, "Krishna", 3))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("not enough seats");
	}

	@Test
	void givenMissingShow_whenBookTickets_thenThrowsNotFound() {
		when(showRepository.findByIdForUpdate(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> bookingService.bookTickets(999L, "Krishna", 1))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("show not found");
	}
}
