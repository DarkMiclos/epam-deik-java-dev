package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {
    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final RoomService roomService;
    
    @ShellMethod(key = "create screening", value = "Creates a new screening. Admin command")
    public String createScreening(String movieName, String roomName, String date) throws ParseException {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(convertStringToDate(date))
                    .build();
            screeningService.createScreening(screeningDto);
            return "Screening created!";
        }
        catch (Exception e) {
            return e.getMessage();
        }
        
    }

    @ShellMethod(key = "delete screening", value = "Deletes an already existing screening. Admin command")
    public String deleteScreening(String movieName, String roomName, String date) throws ParseException {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(convertStringToDate(date))
                    .build();
            screeningService.deleteScreening(screeningDto);
            return "Screening deleted!";
        }
        catch (Exception e) {
            return e.getMessage();
        }
        
    }
    
    @ShellMethod(key = "list screenings", value = "Lists the existing screenings")
    public void listScreenings() {
        List<ScreeningDto> screeningDtoList = screeningService.getScreeningList();
        if(screeningDtoList.isEmpty()) {
            System.out.println("There are no screenings");
        }
        for(var screening : screeningDtoList) {
            System.out.println(screening.getMovie().getName() + " (" + screening.getMovie().getGenre() + ", " 
                    + screening.getMovie().getLengthInMinutes() + " minutes), screened in room " + screening.getRoom().getName() 
                    + ", at " + convertDateToString(screening.getBeginningDateOfScreening()));
        }
    }
    
    private Date convertStringToDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .parse(date);
    }
    
    private String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(date);
    }
}
