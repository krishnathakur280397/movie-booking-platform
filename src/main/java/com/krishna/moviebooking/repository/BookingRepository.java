package com.krishna.moviebooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krishna.moviebooking.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByShowId(Long showId);

	@Query("select b from Booking b join fetch b.show s join fetch s.movie m where b.id = :id")
	Optional<Booking> findByIdWithShowAndMovie(@Param("id") Long id);

	@Query("select b from Booking b join fetch b.show s join fetch s.movie m order by b.createdAt desc")
	List<Booking> findAllWithShowAndMovie();

	@Query("select b from Booking b join fetch b.show s join fetch s.movie m where s.id = :showId order by b.createdAt desc")
	List<Booking> findByShowIdWithShowAndMovie(@Param("showId") Long showId);
}
