package com.krishna.moviebooking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krishna.moviebooking.dto.ShowResponse;
import com.krishna.moviebooking.service.ShowService;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

	private final ShowService showService;

	public ShowController(ShowService showService) {
		this.showService = showService;
	}

	@GetMapping
	public List<ShowResponse> listShows(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		return showService.listShows(from, to);
	}

	@GetMapping("/{id}")
	public ShowResponse getShow(@PathVariable Long id) {
		return showService.getShow(id);
	}
}
