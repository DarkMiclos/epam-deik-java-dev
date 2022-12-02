package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.pricecomponent.PriceComponentService;
import com.epam.training.ticketservice.core.pricecomponent.model.PriceComponentDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.seat.Seat;
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
import java.util.*;

@ShellComponent
@AllArgsConstructor
public class PriceComponentCommand {
    private final PriceComponentService priceComponentService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final UserService userService;
    private final Helper helper = new Helper();

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "update base price",
            value = "Updates the base price for a screening. By default this is 1500 HUF")
    public String updateBasePrice(String newBasePrice) {
        priceComponentService.updateBasePrice(Integer.parseInt(newBasePrice));
        return "The new base price is " + newBasePrice;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "create price component", value = "Creates a new price component. Admin command.")
    public String createPriceComponent(String name, String amount) {
        PriceComponentDto priceComponentDto = PriceComponentDto.builder()
                .withName(name)
                .withAmount(Integer.parseInt(amount))
                .build();
        priceComponentService.createPriceComponent(priceComponentDto);
        return "Price component created!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to room", value = "Attach a price component to a room. Admin command.")
    public String attachPriceComponentToRoom(String priceComponentName, String roomName) {
        priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName);
        return "Price component attached to room!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to movie", value = "Attach a price component to a movie. Admin command.")
    public String attachPriceComponentToMovie(String priceComponentName, String movieName) {
        priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName);
        return "Price component attached to movie!";
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "attach price component to screening",
            value = "Attach a price component to a screening. Admin command.")
    public String attachPriceComponentToScreening(String priceComponentName,
                      String movieName, String roomName, String startDate) {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(helper.convertStringToDate(startDate))
                    .build();
            priceComponentService.attachPriceComponentToScreening(priceComponentName, screeningDto);
            return "Price component attached to screening!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
    @ShellMethod(key = "show price for", value = "Shows the price for a screening.")
    public String showPriceForScreening(String movieName, String roomName, String startDate, String listOfSeats) {
        try {
            Movie movie = movieService.getMovie(movieName);
            Room room = roomService.getRoom(roomName);
            ScreeningDto screeningDto = ScreeningDto.builder()
                    .withMovie(movie)
                    .withRoom(room)
                    .withBeginningDateOfScreening(helper.convertStringToDate(startDate))
                    .build();
            int price = priceComponentService.showPriceForScreening(screeningDto);
            return "The price for this booking would be " 
                    + price * helper.convertStringOfSeatsToList(listOfSeats).size() + " HUF";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describe();
        return user.isPresent() && user.get().getRole() == User.Role.ADMIN
                ? Availability.available()
                : Availability.unavailable("You are not an admin!");
    }
}
