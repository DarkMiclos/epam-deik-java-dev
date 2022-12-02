package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ScreeningServiceImplTest {
    private static final Movie MOVIE = new Movie("Interstellar", "Sci-fi", 169);
    private static final Room ROOM = new Room("asd", 10, 10);
    private static final Date DATE = convertStringToDate("2021-03-15 10:45");
    private static final Date DATE2 = convertStringToDate("2021-03-15 13:37");
    private static final Date DATE3 = convertStringToDate("2021-03-15 10:40");
    private static final Screening ENTITY = new Screening(MOVIE, ROOM, DATE);
    private static final ScreeningDto DTO = ScreeningDto.builder()
            .withMovie(MOVIE)
            .withRoom(ROOM)
            .withBeginningDateOfScreening(DATE)
            .build();
    private static final ScreeningDto DTO2 = ScreeningDto.builder()
            .withMovie(MOVIE)
            .withRoom(ROOM)
            .withBeginningDateOfScreening(DATE2)
            .build();
    private static final ScreeningDto DTO3 = ScreeningDto.builder()
            .withMovie(MOVIE)
            .withRoom(ROOM)
            .withBeginningDateOfScreening(DATE3)
            .build();
    
    private final MovieRepository movieRepository = mock(MovieRepository.class);
    private final RoomRepository roomRepository = mock(RoomRepository.class);
    private final ScreeningRepository screeningRepository = mock(ScreeningRepository.class);
    private final ScreeningServiceImpl underTest = new ScreeningServiceImpl(screeningRepository, movieRepository, roomRepository);
    
    @Test
    void testCreateScreeningShouldThrowExceptionWhenNoMovieWasFound() {
        //Given
        IllegalArgumentException expected = new IllegalArgumentException("There is no movie with this name");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(DTO));
        
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testCreateScreeningShouldThrowExceptionWhenMovieExistsButRoomDoesNot() {
        //Given
        when(movieRepository.findByName("Interstellar")).thenReturn(Optional.of(MOVIE));
        IllegalArgumentException expected = new IllegalArgumentException("There is no room with this name");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(DTO));
        
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
        verify(movieRepository).findByName("Interstellar");
    }
    
    @Test
    void testCreateScreeningShouldCallScreeningRepositoryIfMovieAndRoomExistsAndThereAreNoOverlappingScreenings() {
        //Given
        when(movieRepository.findByName("Interstellar")).thenReturn(Optional.of(MOVIE));
        when(roomRepository.findByName("asd")).thenReturn(Optional.of(ROOM));
        
        //When
        underTest.createScreening(DTO);
        
        //Then
        verify(screeningRepository).save(ENTITY);
    }
    
    @Test
    void testCreateScreeningShouldThrowExceptionWhenThereIsAnOverlappingScreeningBecauseOfSameDate() {
        //Given
        when(movieRepository.findByName("Interstellar")).thenReturn(Optional.of(MOVIE));
        when(roomRepository.findByName("asd")).thenReturn(Optional.of(ROOM));
        when(screeningRepository.findByRoomName("asd")).thenReturn(List.of(ENTITY));
        IllegalArgumentException expected = new IllegalArgumentException("There is an overlapping screening");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(DTO));
        
        //Then
        verify(movieRepository).findByName("Interstellar");
        verify(roomRepository).findByName("asd");
        verify(screeningRepository).findByRoomName("asd");
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    void testCreateScreeningShouldThrowExceptionWhenThereIsAnOverlappingScreeningBecauseEndDateIsBetweenOtherScreening() {
        //Given
        when(movieRepository.findByName("Interstellar")).thenReturn(Optional.of(MOVIE));
        when(roomRepository.findByName("asd")).thenReturn(Optional.of(ROOM));
        when(screeningRepository.findByRoomName("asd")).thenReturn(List.of(ENTITY));
        IllegalArgumentException expected = new IllegalArgumentException("There is an overlapping screening");

        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(DTO3));

        //Then
        verify(movieRepository).findByName("Interstellar");
        verify(roomRepository).findByName("asd");
        verify(screeningRepository).findByRoomName("asd");
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testCreateScreeningShouldThrowExceptionWhenScreeningOverlapsInBreakTime() {
        //Given
        when(movieRepository.findByName("Interstellar")).thenReturn(Optional.of(MOVIE));
        when(roomRepository.findByName("asd")).thenReturn(Optional.of(ROOM));
        when(screeningRepository.findByRoomName("asd")).thenReturn(List.of(ENTITY));
        IllegalArgumentException expected = new IllegalArgumentException("This would start in the break period after another screening in this room");

        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.createScreening(DTO2));

        //Then
        verify(movieRepository).findByName("Interstellar");
        verify(roomRepository).findByName("asd");
        verify(screeningRepository).findByRoomName("asd");
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testDeleteScreeningShouldThrowExceptionWhenScreeningDoesNotExist() {
        //Given
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE)).thenReturn(Optional.empty());
        IllegalArgumentException expected = new IllegalArgumentException("There is no such screening");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteScreening(DTO));
        
        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testDeleteScreeningShouldCallScreeningRepositoryIfScreeningExists() {
        //Given
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE)).thenReturn(Optional.of(ENTITY));
        
        //When
        underTest.deleteScreening(DTO);
        
        //Then
        verify(screeningRepository).delete(ENTITY);
    }
    
    @Test
    void testGetScreeningListShouldReturnAllScreenings() {
        //Given
        when(screeningRepository.findAll()).thenReturn(List.of(ENTITY));
        List<ScreeningDto> expected = List.of(DTO);

        //When
        List<ScreeningDto> actual = underTest.getScreeningList();

        //Then
        assertEquals(expected, actual);
        verify(screeningRepository).findAll();
    }
    
    @Test
    void testGetScreeningShouldThrowExceptionWhenScreeningIsNotFound() {
        //Given
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE)).thenReturn(Optional.empty());
        IllegalArgumentException expected = new IllegalArgumentException("There is no such screening");
        
        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.getScreening(MOVIE, ROOM, DATE));
        
        //Then
        verify(screeningRepository).findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE);
        assertEquals(expected.getMessage(), actual.getMessage());
    }
    
    @Test
    void testGetScreeningShouldReturnScreeningWhenParametersAreCorrectAndScreeningExists() {
        //Given
        when(screeningRepository.findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE)).thenReturn(Optional.of(ENTITY));
        Screening expected = ENTITY;
        
        //When
        Screening actual = underTest.getScreening(MOVIE, ROOM, DATE);
        
        //Then
        verify(screeningRepository).findByMovieAndRoomAndBeginningDateOfScreening(MOVIE, ROOM, DATE);
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
