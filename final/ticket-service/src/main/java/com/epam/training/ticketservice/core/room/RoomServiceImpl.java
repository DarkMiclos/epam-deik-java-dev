package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        Room room = getRoom(roomDto.getName());
        room.setNumberOfRows(roomDto.getNumberOfRows());
        room.setNumberOfColumns(roomDto.getNumberOfColumns());
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(String name) {
        Room room = getRoom(name);
        roomRepository.delete(room);
    }

    @Override
    public List<RoomDto> getRoomList() {
        return roomRepository.findAll().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Room getRoom(String roomName) {
        Optional<Room> optionalRoom = roomRepository.findByName(roomName);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("There is no such room");
        } else {
            return  optionalRoom.get();
        }
    }

    private RoomDto convertEntityToDto(Room room) {
        return RoomDto.builder()
                .withName(room.getName())
                .withNumberOfRows(room.getNumberOfRows())
                .withNumberOfColumns(room.getNumberOfColumns())
                .build();
    }
}
