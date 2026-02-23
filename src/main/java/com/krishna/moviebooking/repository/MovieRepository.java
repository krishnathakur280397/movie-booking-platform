package com.krishna.moviebooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krishna.moviebooking.entity.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
