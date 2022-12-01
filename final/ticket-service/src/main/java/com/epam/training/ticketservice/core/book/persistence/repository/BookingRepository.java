package com.epam.training.ticketservice.core.book.persistence.repository;

import com.epam.training.ticketservice.core.book.persistence.entity.Booking;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByScreening_Room(Room room);
    
    List<Booking> findByUser_Username(String username);
}
