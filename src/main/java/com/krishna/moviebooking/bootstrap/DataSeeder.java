package com.krishna.moviebooking.bootstrap;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.krishna.moviebooking.entity.Movie;
import com.krishna.moviebooking.entity.Show;
import com.krishna.moviebooking.repository.MovieRepository;
import com.krishna.moviebooking.repository.ShowRepository;

@Component
public class DataSeeder implements CommandLineRunner {

	private final MovieRepository movieRepository;
	private final ShowRepository showRepository;

	public DataSeeder(MovieRepository movieRepository, ShowRepository showRepository) {
		this.movieRepository = movieRepository;
		this.showRepository = showRepository;
	}

	@Override
	public void run(String... args) {
		if (showRepository.count() > 0) {
			return;
		}

		Movie inception = movieRepository.save(new Movie("Inception"));
		Movie interstellar = movieRepository.save(new Movie("Interstellar"));

		showRepository.save(new Show(inception, "Screen-1", LocalDateTime.now().plusHours(2), 50));
		showRepository.save(new Show(inception, "Screen-2", LocalDateTime.now().plusDays(1).withHour(18).withMinute(0), 80));
		showRepository.save(new Show(interstellar, "Screen-1", LocalDateTime.now().plusDays(1).withHour(21).withMinute(0), 60));
	}
}
