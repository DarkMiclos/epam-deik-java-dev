package com.epam.training.ticketservice.core.pricecomponent;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.pricecomponent.persistence.entity.PriceComponent;
import com.epam.training.ticketservice.core.pricecomponent.persistence.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.ui.command.Helper;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PriceComponentServiceImplTest {
    private static final Movie MOVIE = new Movie("Interstellar", "Sci-fi", 169);
    private static final Room ROOM = new Room("asd", 10, 10);
    private static final Date DATE = convertStringToDate("2021-03-15 10:45");
    private static final Screening SCREENING = new Screening(MOVIE, ROOM, DATE);
    private static final ScreeningDto SCREENING_DTO = ScreeningDto.builder()
            .withMovie(MOVIE)
            .withRoom(ROOM)
            .withBeginningDateOfScreening(DATE)
            .build();
    
    private final PriceComponent ENTITY = new PriceComponent("BasePrice", 1500);
    private final PriceComponent ENTITY2 = new PriceComponent("BasePrice", 2000);
    private final PriceComponent ENTITY3 = new PriceComponent("Component", 3000);
    private final PriceComponent ENTITY4 = new PriceComponent("Component", 3000,
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>());
    private final PriceComponent PRICE_COMPONENT_FOR_MOVIE = new PriceComponent("movie", 500);
    private final PriceComponent PRICE_COMPONENT_FOR_ROOM = new PriceComponent("room", 500);
    private final PriceComponent PRICE_COMPONENT_FOR_SCREENING = new PriceComponent("screening", 500);
    private final PriceComponentDto DTO = PriceComponentDto.builder()
            .withName(ENTITY.getName())
            .withAmount(ENTITY.getAmount())
            .build();

    private final PriceComponentDto DTO2 = PriceComponentDto.builder()
            .withName(ENTITY3.getName())
            .withAmount(ENTITY3.getAmount())
            .build();
    
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final PriceComponentRepository priceComponentRepository = mock(PriceComponentRepository.class);
    private final PriceComponentService underTest = new PriceComponentServiceImpl(priceComponentRepository,
            roomRepository,
            movieRepository,
            screeningRepository);
    
    @Test
    void testUpdateBasePriceShouldUpdateBasePrice() {
        //Given
        when(priceComponentRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));
        int expected = 2000;
        
        //When
        underTest.updateBasePrice(expected);
        int actual = underTest.getBasePrice();
        
        //Then
        verify(priceComponentRepository, atLeast(2)).findByName(ENTITY.getName());
        verify(priceComponentRepository).save(ENTITY2);
        assertEquals(expected, actual);
    }
    
    @Test
    void testGetBasePriceShouldReturnZeroIfNotInitialized() {
        //Given
        when(priceComponentRepository.findByName("BasePrice")).thenReturn(Optional.empty());
        int expected = 0;
        
        //When
        int actual = underTest.getBasePrice();
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    void testCreatePriceComponentCallsPriceComponentRepositoryToSave() {
        //Given
        //When
        underTest.createPriceComponent(DTO2);
        
        //Then
        verify(priceComponentRepository).save(ENTITY3);
    }
    
    @Test
    void testAttachPriceComponentToRoomAttachesPriceComponentToRoomIfTheyBothExist() {
        //Given
        when(priceComponentRepository.findByName(ENTITY4.getName())).thenReturn(Optional.of(ENTITY4));
        when(roomRepository.findByName(ROOM.getName())).thenReturn(Optional.of(ROOM));
        Set<Room> expected = Set.of(ROOM);
        
        //When
        underTest.attachPriceComponentToRoom(ENTITY4.getName(), ROOM.getName());
        Set<Room> actual = ENTITY4.getRooms();
        
        //Then
        verify(priceComponentRepository).findByName(ENTITY4.getName());
        verify(roomRepository).findByName(ROOM.getName());
        verify(priceComponentRepository).save(ENTITY4);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToMovieAttachesPriceComponentToMovieIfTheyBothExist() {
        //Given
        when(priceComponentRepository.findByName(ENTITY4.getName())).thenReturn(Optional.of(ENTITY4));
        when(movieRepository.findByName(MOVIE.getName())).thenReturn(Optional.of(MOVIE));
        Set<Movie> expected = Set.of(MOVIE);

        //When
        underTest.attachPriceComponentToMovie(ENTITY4.getName(), MOVIE.getName());
        Set<Movie> actual = ENTITY4.getMovies();

        //Then
        verify(priceComponentRepository).findByName(ENTITY4.getName());
        verify(movieRepository).findByName(MOVIE.getName());
        verify(priceComponentRepository).save(ENTITY4);
        assertEquals(expected, actual);
    }

    @Test
    void testAttachPriceComponentToScreeningAttachesPriceComponentToScreeningIfTheyBothExist() {
        //Given
        when(priceComponentRepository.findByName(ENTITY4.getName())).thenReturn(Optional.of(ENTITY4));
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(
                MOVIE,
                ROOM,
                DATE))
                .thenReturn(Optional.of(SCREENING));
        Set<Screening> expected = Set.of(SCREENING);

        //When
        underTest.attachPriceComponentToScreening(ENTITY4.getName(), SCREENING_DTO);
        Set<Screening> actual = ENTITY4.getScreenings();

        //Then
        verify(priceComponentRepository).findByName(ENTITY4.getName());
        verify(screeningRepository).findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE);
        verify(priceComponentRepository).save(ENTITY4);
        assertEquals(expected, actual);
    }
    
    @Test
    void testShowPriceForScreeningShouldCalculatePriceBasedOnPriceComponents() {
        //Given
        when(priceComponentRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));
        when(priceComponentRepository.findByMovies(MOVIE)).thenReturn(Optional.of(PRICE_COMPONENT_FOR_MOVIE));
        when(priceComponentRepository.findByRooms(ROOM)).thenReturn(Optional.of(PRICE_COMPONENT_FOR_ROOM));
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE)).thenReturn(Optional.of(SCREENING));
        when(priceComponentRepository.findByScreenings(SCREENING)).thenReturn(Optional.of(PRICE_COMPONENT_FOR_SCREENING));
        int expected = 3000;
        
        //When
        int actual = underTest.showPriceForScreening(SCREENING_DTO);
       
        //Then
        verify(priceComponentRepository).findByName(ENTITY.getName());
        verify(priceComponentRepository).findByMovies(MOVIE);
        verify(priceComponentRepository).findByRooms(ROOM);
        verify(screeningRepository).findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE);
        verify(priceComponentRepository).findByScreenings(SCREENING);
        assertEquals(expected, actual);
    }
    
    @Test
    void testShowPriceForScreeningShouldOnlyReturnBasePriceIfThereAreNoAdditionalPriceComponents() {
        //Given
        when(priceComponentRepository.findByName(ENTITY.getName())).thenReturn(Optional.of(ENTITY));
        when(priceComponentRepository.findByMovies(MOVIE)).thenReturn(Optional.empty());
        when(priceComponentRepository.findByRooms(ROOM)).thenReturn(Optional.empty());
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE)).thenReturn(Optional.of(SCREENING));
        when(priceComponentRepository.findByScreenings(SCREENING)).thenReturn(Optional.empty());
        int expected = 1500;
        
        //When
        int actual = underTest.showPriceForScreening(SCREENING_DTO);
        
        //Then
        verify(priceComponentRepository).findByName(ENTITY.getName());
        verify(priceComponentRepository).findByMovies(MOVIE);
        verify(priceComponentRepository).findByRooms(ROOM);
        verify(screeningRepository).findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE);
        verify(priceComponentRepository).findByScreenings(SCREENING);
        assertEquals(expected, actual);
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
