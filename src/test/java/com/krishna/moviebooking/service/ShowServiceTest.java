package com.krishna.moviebooking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.krishna.moviebooking.dto.ShowResponse;
import com.krishna.moviebooking.entity.Movie;
import com.krishna.moviebooking.entity.Show;
import com.krishna.moviebooking.repository.ShowRepository;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

	@Mock
	ShowRepository showRepository;

	@InjectMocks
	ShowService showService;

	@Test
	void givenShows_whenListShows_thenReturnsMappedResponses() {
		Movie movie = new Movie("Inception");
		Show show = new Show(movie, "Screen-1", LocalDateTime.now().plusHours(2), 50);
		ReflectionTestUtils.setField(show, "id", 1L);

		when(showRepository.findShows(null, null)).thenReturn(List.of(show));

		List<ShowResponse> responses = showService.listShows(null, null);

		assertThat(responses).hasSize(1);
		assertThat(responses.get(0).getId()).isEqualTo(1L);
		assertThat(responses.get(0).getMovieTitle()).isEqualTo("Inception");
		assertThat(responses.get(0).getAvailableSeats()).isEqualTo(50);
	}

	@Test
	void givenExistingShow_whenGetShow_thenReturnsResponse() {
		Movie movie = new Movie("Interstellar");
		Show show = new Show(movie, "Screen-2", LocalDateTime.now().plusHours(3), 60);
		ReflectionTestUtils.setField(show, "id", 5L);

		when(showRepository.findByIdWithMovie(5L)).thenReturn(Optional.of(show));

		ShowResponse response = showService.getShow(5L);

		assertThat(response.getId()).isEqualTo(5L);
		assertThat(response.getMovieTitle()).isEqualTo("Interstellar");
		assertThat(response.getTotalSeats()).isEqualTo(60);
	}

	@Test
	void givenMissingShow_whenGetShow_thenThrowsNotFound() {
		when(showRepository.findByIdWithMovie(123L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> showService.getShow(123L))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("show not found");
	}
}
