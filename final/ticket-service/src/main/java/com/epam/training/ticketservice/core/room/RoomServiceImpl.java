package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public void createRoom(RoomDto roomDto) {
        Room room = new Room(roomDto.getName(),
                roomDto.getNumberOfRows(),
                roomDto.getNumberOfColumns());
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Room room = roomRepository.findByName(roomDto.getName()).get();
        room.setNumberOfRows(roomDto.getNumberOfRows());
        room.setNumberOfColumns(roomDto.getNumberOfColumns());
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        Room room = roomRepository.findByName(name).get();
        roomRepository.delete(room);
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .withName(room.getName())
                .withNumberOfRows(room.getNumberOfRows())
                .withNumberOfColumns(room.getNumberOfColumns())
                .build();
    }
}
