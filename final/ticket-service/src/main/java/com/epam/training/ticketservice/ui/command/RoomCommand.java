package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class RoomCommand {
    private final RoomService roomService;
    
    @ShellMethod(key = "create room", value = "Creates a room. Admin command")
    public RoomDto createRoom(String name, Integer numberOfRows, Integer numberOfColumns) {
        RoomDto roomDto = RoomDto.builder()
                .withName(name)
                .withNumberOfRows(numberOfRows)
                .withNumberOfColumns(numberOfColumns)
                .build();
        roomService.createRoom(roomDto);
        return roomDto;
    }

    @ShellMethod(key = "update room", value = "Updates an already existing room. Admin command")
    public RoomDto updateRoom(String name, Integer numberOfRows, Integer numberOfColumns) {
        RoomDto roomDto = RoomDto.builder()
                .withName(name)
                .withNumberOfRows(numberOfRows)
                .withNumberOfColumns(numberOfColumns)
                .build();
        roomService.updateRoom(roomDto);
        return roomDto;
    }
    
    @ShellMethod(key = "delete room", value = "Deletes an existing room. Admin command")
    public void deleteRoom(String name) {
        roomService.deleteRoom(name);
    }
    
    @ShellMethod(key = "list rooms", value = "Lists all of the existing rooms.")
    public void listRooms() {
        List<RoomDto> roomDtoList = roomService.getRoomList();
        if(roomDtoList.isEmpty()) {
            System.out.println("There are no rooms at the moment");
        }
        for (var roomDto : roomDtoList) {
            System.out.println("Room " + roomDto.getName() + " with " + roomDto.getNumberOfSeats() + " seats, " + roomDto.getNumberOfRows() 
                    + " rows and " + roomDto.getNumberOfColumns() + " columns");
        }
    }
}
