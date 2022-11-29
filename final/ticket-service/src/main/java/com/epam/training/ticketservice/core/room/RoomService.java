package com.epam.training.ticketservice.core.room;

import com.epam.training.ticketservice.core.room.model.RoomDto;

import java.util.List;

public interface RoomService {
    void createRoom(RoomDto roomDto);
    
    void updateRoom(RoomDto roomDto);
    
    void deleteRoom(String name);

    List<RoomDto> getRoomList();
}
