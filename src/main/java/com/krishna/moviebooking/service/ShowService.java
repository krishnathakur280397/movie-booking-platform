package com.krishna.moviebooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.krishna.moviebooking.dto.ShowResponse;
import com.krishna.moviebooking.entity.Show;
import com.krishna.moviebooking.repository.ShowRepository;

@Service
public class ShowService {

	private final ShowRepository showRepository;

	public ShowService(ShowRepository showRepository) {
		this.showRepository = showRepository;
	}

	@Transactional(readOnly = true)
	public List<ShowResponse> listShows(LocalDateTime from, LocalDateTime to) {
		return showRepository.findShows(from, to).stream().map(ShowService::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public ShowResponse getShow(Long id) {
		Show show = showRepository.findByIdWithMovie(id)
				.orElseThrow(() -> new IllegalArgumentException("show not found: " + id));
		return toResponse(show);
	}

	private static ShowResponse toResponse(Show show) {
		return new ShowResponse(show.getId(), show.getMovie().getTitle(), show.getScreen(), show.getShowTime(),
				show.getTotalSeats(), show.getAvailableSeats());
	}
}
