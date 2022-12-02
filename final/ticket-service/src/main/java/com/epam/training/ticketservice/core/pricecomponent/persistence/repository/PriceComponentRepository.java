package com.epam.training.ticketservice.core.pricecomponent.persistence.repository;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceComponentRepository extends JpaRepository<PriceComponent, Integer> {
    Optional<PriceComponent> findByName(String name);
    
    Optional<PriceComponent> findByRooms(Room room);
    
    Optional<PriceComponent> findByMovies(Movie movie);
    
    Optional<PriceComponent> findByScreenings(Screening screening);
}
