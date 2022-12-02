package com.epam.training.ticketservice.core.booking;

import com.epam.training.ticketservice.core.booking.model.BookingDto;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.seat.Seat;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.epam.training.ticketservice.core.user.persistence.entity.User.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {
    private static final Movie MOVIE = new Movie("Interstellar", "Sci-fi", 169);
    private static final Room ROOM = new Room("asd", 10, 10);
    private static final Date DATE = convertStringToDate("2021-03-15 10:45");
    private static final Screening SCREENING = new Screening(MOVIE, ROOM, DATE);
    
    private static final Seat CORRECT_SEAT = new Seat(1,1);
    private static final Seat INVALID_SEAT = new Seat(11,1);
    private static final List<Seat> CORRECT_LIST = List.of(CORRECT_SEAT);
    private static final List<Seat> INVALID_LIST = List.of(INVALID_SEAT);
    private static final User USER2 = new User("user", "user", USER);
    private static final Booking CORRECT_ENTITY = new Booking(SCREENING, CORRECT_LIST, USER2, 1500);
    
    private static final BookingDto CORRECT_DTO = BookingDto.builder()
            .withScreening(SCREENING)
            .withListOfSeat(CORRECT_LIST)
            .withPrice(1500)
            .build();
    
    private static final Booking INVALID_ENTITY = new Booking(SCREENING, INVALID_LIST, USER2, 1500);
    private static final BookingDto INVALID_DTO = BookingDto.builder()
            .withScreening(SCREENING)
            .withListOfSeat(INVALID_LIST)
            .withPrice(1500)
            .build();
    
    private static final UserDto USER_DTO = new UserDto(USER2.getUsername(), USER2.getRole());
    
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = mock(UserService.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final BookingServiceImpl underTest = new BookingServiceImpl(bookingRepository, userRepository, userService);
    
    @Test
    void testCreateBookingShouldThrowExceptionIfSeatDoesNotExist() {
        //Given
        IllegalArgumentException expected = new IllegalArgumentException("Seat "
                + INVALID_SEAT.toString() + " does not exist in this room");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createBooking(INVALID_DTO));
        
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testCreateBookingShouldThrowExceptionWhenSeatIsTaken() {
        //Given
        when(bookingRepository.findByScreening_Room(CORRECT_DTO.getScreening().getRoom())).thenReturn(List.of(CORRECT_ENTITY));
        IllegalArgumentException expected = new IllegalArgumentException("Seat "
                + CORRECT_SEAT.toString() + " is already taken");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createBooking(CORRECT_DTO));
        
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testCreateBookingShouldCallBookingRepositoryIfScreeningExistsAndSeatIsCorrect() {
        //Given
        when(userService.describe()).thenReturn(Optional.of(USER_DTO));
        when(userRepository.findByUsername(USER2.getUsername())).thenReturn(Optional.of(USER2));
        
        //When
        underTest.createBooking(CORRECT_DTO);
        
        //Then
        verify(userService).describe();
        verify(userRepository).findByUsername(USER2.getUsername());
        verify(bookingRepository).save(CORRECT_ENTITY);
    }

    static Date convertStringToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                    .parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
