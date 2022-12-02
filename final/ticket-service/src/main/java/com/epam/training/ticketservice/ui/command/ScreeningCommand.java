package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ShellComponent
@AllArgsConstructor
public class ScreeningCommand {
    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final UserService userService;
    private final Helper helper = new Helper();

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create screening", value = "Creates a new screening. Admin command")
    public String createScreening(String movieName, String roomName, String date) throws ParseException {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(helper.convertStringToDate(date))
                    .build();
            screeningService.createScreening(screeningDto);
            return "Screening created!";
        } catch (Exception e) {
            return e.getMessage();
        }
        
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "delete screening", value = "Deletes an already existing screening. Admin command")
    public String deleteScreening(String movieName, String roomName, String date) throws ParseException {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(helper.convertStringToDate(date))
                    .build();
            screeningService.deleteScreening(screeningDto);
            return "Screening deleted!";
        } catch (Exception e) {
            return e.getMessage();
        }
        
    }
    
    @ShellMethod(key = "list screenings", value = "Lists the existing screenings")
    public void listScreenings() {
        List<ScreeningDto> screeningDtoList = screeningService.getScreeningList();
        if (screeningDtoList.isEmpty()) {
            System.out.println("There are no screenings");
        }
        for (var screening : screeningDtoList) {
            System.out.println(screening.getMovie().getName() 
                    + " (" + screening.getMovie().getGenre() + ", " 
                    + screening.getMovie().getLengthInMinutes() 
                    + " minutes), screened in room " 
                    + screening.getRoom().getName() 
                    + ", at " + helper.convertDateToString(screening.getBeginningDateOfScreening()));
        }
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().getRole() == User.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
