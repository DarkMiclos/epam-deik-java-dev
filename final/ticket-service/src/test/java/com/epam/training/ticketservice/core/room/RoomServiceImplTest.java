package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomServiceImplTest {
    private static final Room ENTITY = new Room("asd", 10, 10);
    private static final RoomDto DTO = RoomDto.builder()
            .withName("asd")
            .withNumberOfRows(10)
            .withNumberOfColumns(10)
            .build();
    private final RoomRepository roomRepository = Mockito.mock(RoomRepository.class);
    private final RoomServiceImpl underTest = new RoomServiceImpl(roomRepository);
    
    @Test
    void testGetRoomShouldThrowIllegalArgumentExceptionWhenARoomIsNotFound() {
        //Given
        IllegalArgumentException expected = new IllegalArgumentException("There is no such room");

        //When
        IllegalArgumentException actual = assertThrows(IllegalArgumentException.class,
                () -> underTest.getRoom("dummy"));

        //Then
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    void testGetRoomShouldReturnRoomWhenParametersAreCorrectAndRoomExists() {
        //Given
        when(roomRepository.findByName(DTO.getName())).thenReturn(Optional.of(ENTITY));
        Room expected = ENTITY;

        //When
        Room actual = underTest.getRoom(DTO.getName());

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).findByName(DTO.getName());
    }
    
    @Test
    void testGetRoomListShouldReturnAllRooms() {
        //Given
        when(roomRepository.findAll()).thenReturn(List.of(ENTITY));
        List<RoomDto> expected = List.of(DTO);

        //When
        List<RoomDto> actual = underTest.getRoomList();

        //Then
        assertEquals(expected, actual);
        verify(roomRepository).findAll();
    }
}
