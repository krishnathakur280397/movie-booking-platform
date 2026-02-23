package com.krishna.moviebooking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krishna.moviebooking.entity.Show;

import jakarta.persistence.LockModeType;

public interface ShowRepository extends JpaRepository<Show, Long> {

	@Query("select s from Show s join fetch s.movie m where (:from is null or s.showTime >= :from) and (:to is null or s.showTime <= :to) order by s.showTime asc")
	List<Show> findShows(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

	@Query("select s from Show s join fetch s.movie m where s.id = :id")
	Optional<Show> findByIdWithMovie(@Param("id") Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from Show s join fetch s.movie m where s.id = :id")
	Optional<Show> findByIdForUpdate(@Param("id") Long id);
}
